package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.application.service.VehicleSmartCreationService;
import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.PatchVehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleInclusionEntity;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.VehicleInclusionR2dbcRepository;
import com.yowyob.template.infrastructure.mappers.VehicleMapper;
import com.yowyob.template.infrastructure.security.AuthenticatedUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final ManageVehicleUseCase manageVehicleUseCase;
    private final VehicleMapper mapper;
    private final VehicleSmartCreationService smartCreationService;
    private final VehicleInclusionR2dbcRepository inclusionRepository;

    // =================================================================================================
    // 1. SMART CREATION (Mode Simplifié)
    // =================================================================================================

    @PostMapping("/simplified")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule (Mode Simplifié - Smart)", 
               description = "Crée un véhicule en utilisant des noms (ex: 'Toyota') au lieu d'IDs. " +
                             "Le véhicule est automatiquement assigné à l'utilisateur connecté.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Véhicule créé et assigné"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })
    public Mono<VehicleResponse> createVehicleSimplified(
            @Valid @RequestBody SimplifiedVehicleRequest request,
            @AuthenticationPrincipal AuthenticatedUser user) {
        
        // Sécurité : On vérifie que le token est bien présent et valide
        if (user == null || user.getId() == null) {
            log.error("Tentative de création simplifiée sans utilisateur authentifié");
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous devez être authentifié pour créer un véhicule"));
        }
        
        UUID partyId = UUID.fromString(user.getId());

        // On délègue au service d'application qui gère les lookups et la liaison
        return smartCreationService.createVehicleFromNames(request, partyId)
                .flatMap(this::buildVehicleResponse);
    }

    // =================================================================================================
    // 2. CREATION STANDARD (Mode IDs)
    // =================================================================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule (Mode Standard IDs)", description = "Nécessite de connaître les IDs des référentiels (Marque, Modèle...).")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "ID invalide ou manquant")
    })
    public Mono<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return Mono.just(mapper.toDomain(vehicleRequest))
                .flatMap(manageVehicleUseCase::createVehicle)
                .flatMap(vehicle -> saveInclusions(vehicle.vehicleId(), vehicleRequest)
                        .then(buildVehicleResponse(vehicle)));
    }

    // =================================================================================================
    // 3. LECTURE (GET)
    // =================================================================================================

    @GetMapping
    @Operation(summary = "Lister mes véhicules", description = "Retourne uniquement les véhicules dont je suis propriétaire/utilisateur.")
    @SecurityRequirement(name = "bearerAuth")
    public Flux<VehicleResponse> getMyVehicles(@AuthenticationPrincipal AuthenticatedUser user) {
        if (user == null || user.getId() == null) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));
        }
        UUID partyId = UUID.fromString(user.getId());
        return manageVehicleUseCase.getVehiclesByOwner(partyId)
                .flatMap(this::buildVehicleResponse);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lister les véhicules d'un utilisateur", description = "Retourne les véhicules associés à l'utilisateur indiqué.")
    public Flux<VehicleResponse> getVehiclesByUserId(@PathVariable UUID userId) {
        return manageVehicleUseCase.getVehiclesByOwner(userId)
                .flatMap(this::buildVehicleResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un véhicule par son ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule non trouvé")
    })
    public Mono<VehicleResponse> getVehicleById(@PathVariable UUID id) {
        return manageVehicleUseCase.getVehicleById(id)
                .flatMap(this::buildVehicleResponse);
    }

    // =================================================================================================
    // 4. MISE A JOUR (PUT & PATCH)
    // =================================================================================================

    @PutMapping("/{id}")
    @Operation(summary = "Remplacement complet (PUT)", description = "Remplace intégralement le véhicule. Attention : les champs non fournis seront mis à NULL.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Véhicule mis à jour"),
            @ApiResponse(responseCode = "404", description = "Véhicule non trouvé")
    })
    public Mono<VehicleResponse> updateVehicle(
            @Parameter(description = "ID du véhicule") @PathVariable UUID id,
            @Valid @RequestBody VehicleRequest r) {
        
        Vehicle d = mapper.toDomain(r);
        // On reconstruit l'objet domaine en forçant l'ID de l'URL
        Vehicle v = new Vehicle(
                id, d.vehicleMakeId(), d.vehicleModelId(), d.transmissionTypeId(), d.manufacturerId(), d.vehicleSizeId(), d.vehicleTypeId(), d.fuelTypeId(), 
                d.vehicleSerialNumber(), d.vehicleSerialPhoto(), d.registrationNumber(), d.registrationPhoto(), d.registrationExpiryDate(), 
                d.tankCapacity(), d.luggageMaxCapacity(), d.totalSeatNumber(), d.averageFuelConsumptionPerKm(), d.mileageAtStart(), d.mileageSinceCommissioning(), 
                d.vehicleAgeAtStart(), d.brand(), null, null
        );

        return manageVehicleUseCase.updateVehicle(v)
                .flatMap(vehicle -> replaceInclusions(vehicle.vehicleId(), r)
                        .then(buildVehicleResponse(vehicle)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mise à jour partielle (PATCH)", description = "Met à jour uniquement les champs fournis. Idéal pour changer le kilométrage.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Véhicule mis à jour"),
            @ApiResponse(responseCode = "404", description = "Véhicule non trouvé")
    })
    public Mono<VehicleResponse> patchVehicle(
            @Parameter(description = "ID du véhicule") @PathVariable UUID id,
            @RequestBody PatchVehicleRequest patchRequest) {
        
        // Mapping manuel DTO -> Domain Partiel pour éviter les écrasements par null du Mapper
        Vehicle partialVehicle = new Vehicle(
                null, // ID ignoré ici, géré par le service
                patchRequest.vehicleMakeId(), patchRequest.vehicleModelId(), patchRequest.transmissionTypeId(), 
                patchRequest.manufacturerId(), patchRequest.vehicleSizeId(), patchRequest.vehicleTypeId(), 
                patchRequest.fuelTypeId(), patchRequest.vehicleSerialNumber(), patchRequest.vehicleSerialPhoto(), 
                patchRequest.registrationNumber(), patchRequest.registrationPhoto(), patchRequest.registrationExpiryDate(), 
                patchRequest.tankCapacity(), patchRequest.luggageMaxCapacity(), patchRequest.totalSeatNumber(), 
                patchRequest.averageFuelConsumptionPerKm(), patchRequest.mileageAtStart(), patchRequest.mileageSinceCommissioning(), 
                patchRequest.vehicleAgeAtStart(), patchRequest.brand(), null, null
        );

        return manageVehicleUseCase.patchVehicle(id, partialVehicle)
                .flatMap(vehicle -> patchInclusions(vehicle.vehicleId(), patchRequest)
                        .then(buildVehicleResponse(vehicle)));
    }

    // =================================================================================================
    // 5. SUPPRESSION (DELETE)
    // =================================================================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un véhicule")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "404", description = "Véhicule non trouvé")
    })
    public Mono<Void> deleteVehicle(@PathVariable UUID id) {
        return manageVehicleUseCase.deleteVehicle(id);
    }

    private Mono<Void> saveInclusions(UUID vehicleId, VehicleRequest request) {
        return replaceInclusions(vehicleId, request);
    }

    private Mono<Void> replaceInclusions(UUID vehicleId, VehicleRequest request) {
        return inclusionRepository.deleteByVehicleId(vehicleId)
                .thenMany(buildInclusions(vehicleId, request))
                .flatMap(inclusionRepository::save)
                .then();
    }

    private Mono<Void> patchInclusions(UUID vehicleId, PatchVehicleRequest request) {
        return buildInclusions(vehicleId, request)
                .flatMap(inclusion -> inclusionRepository.deleteByVehicleIdAndInclusionName(vehicleId, inclusion.getInclusionName())
                        .then(inclusionRepository.save(inclusion)))
                .then();
    }

    private Mono<VehicleResponse> buildVehicleResponse(Vehicle vehicle) {
        return inclusionRepository.findByVehicleId(vehicle.vehicleId())
                .collectList()
                .map(inclusions -> toResponseWithInclusions(vehicle, inclusions));
    }

    private VehicleResponse toResponseWithInclusions(Vehicle vehicle, List<VehicleInclusionEntity> inclusions) {
        return new VehicleResponse(
                vehicle.vehicleId(),
                vehicle.vehicleMakeId(),
                vehicle.vehicleModelId(),
                vehicle.transmissionTypeId(),
                vehicle.manufacturerId(),
                vehicle.vehicleSizeId(),
                vehicle.vehicleTypeId(),
                vehicle.fuelTypeId(),
                vehicle.vehicleSerialNumber(),
                vehicle.vehicleSerialPhoto(),
                vehicle.registrationNumber(),
                vehicle.registrationPhoto(),
                vehicle.registrationExpiryDate(),
                vehicle.tankCapacity(),
                vehicle.luggageMaxCapacity(),
                vehicle.totalSeatNumber(),
                vehicle.averageFuelConsumptionPerKm(),
                vehicle.mileageAtStart(),
                vehicle.mileageSinceCommissioning(),
                vehicle.vehicleAgeAtStart(),
                vehicle.brand(),
                vehicle.createdAt(),
                vehicle.updatedAt(),
                inclusionValue(inclusions, "Air-conditioned"),
                inclusionValue(inclusions, "Comfortable"),
                inclusionValue(inclusions, "Soft"),
                inclusionValue(inclusions, "Screen"),
                inclusionValue(inclusions, "Wifi"),
                inclusionValue(inclusions, "Toll charge"),
                inclusionValue(inclusions, "Car Parking"),
                inclusionValue(inclusions, "Alarm"),
                inclusionValue(inclusions, "State tax"),
                inclusionValue(inclusions, "Driver Allowance"),
                inclusionValue(inclusions, "Pickup and drop"),
                inclusionValue(inclusions, "Internet"),
                inclusionValue(inclusions, "Pets Allow")
        );
    }

    private Boolean inclusionValue(List<VehicleInclusionEntity> inclusions, String name) {
        return inclusions.stream()
                .filter(inclusion -> name.equalsIgnoreCase(inclusion.getInclusionName()))
                .map(VehicleInclusionEntity::getIsIncluded)
                .findFirst()
                .orElse(false);
    }

    private reactor.core.publisher.Flux<VehicleInclusionEntity> buildInclusions(UUID vehicleId, VehicleRequest request) {
        return buildInclusions(vehicleId, request.airConditioned(), request.comfortable(), request.soft(), request.screen(),
                request.wifi(), request.tollCharge(), request.carParking(), request.alarm(), request.stateTax(),
                request.driverAllowance(), request.pickupAndDrop(), request.internet(), request.petsAllow());
    }

    private reactor.core.publisher.Flux<VehicleInclusionEntity> buildInclusions(UUID vehicleId, PatchVehicleRequest request) {
        return buildInclusions(vehicleId, request.airConditioned(), request.comfortable(), request.soft(), request.screen(),
                request.wifi(), request.tollCharge(), request.carParking(), request.alarm(), request.stateTax(),
                request.driverAllowance(), request.pickupAndDrop(), request.internet(), request.petsAllow());
    }

    private reactor.core.publisher.Flux<VehicleInclusionEntity> buildInclusions(
            UUID vehicleId,
            Boolean airConditioned,
            Boolean comfortable,
            Boolean soft,
            Boolean screen,
            Boolean wifi,
            Boolean tollCharge,
            Boolean carParking,
            Boolean alarm,
            Boolean stateTax,
            Boolean driverAllowance,
            Boolean pickupAndDrop,
            Boolean internet,
            Boolean petsAllow) {
        return reactor.core.publisher.Flux.just(
                        inclusion(vehicleId, "Air-conditioned", airConditioned),
                        inclusion(vehicleId, "Comfortable", comfortable),
                        inclusion(vehicleId, "Soft", soft),
                        inclusion(vehicleId, "Screen", screen),
                        inclusion(vehicleId, "Wifi", wifi),
                        inclusion(vehicleId, "Toll charge", tollCharge),
                        inclusion(vehicleId, "Car Parking", carParking),
                        inclusion(vehicleId, "Alarm", alarm),
                        inclusion(vehicleId, "State tax", stateTax),
                        inclusion(vehicleId, "Driver Allowance", driverAllowance),
                        inclusion(vehicleId, "Pickup and drop", pickupAndDrop),
                        inclusion(vehicleId, "Internet", internet),
                        inclusion(vehicleId, "Pets Allow", petsAllow))
                .filter(java.util.Objects::nonNull);
    }

    private VehicleInclusionEntity inclusion(UUID vehicleId, String name, Boolean isIncluded) {
        if (isIncluded == null) {
            return null;
        }
        return VehicleInclusionEntity.builder()
                .vehicleId(vehicleId)
                .inclusionName(name)
                .isIncluded(isIncluded)
                .build();
    }
}