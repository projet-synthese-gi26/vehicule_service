package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.application.service.VehicleSmartCreationService;
import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.PatchVehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleResponse;
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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final ManageVehicleUseCase manageVehicleUseCase;
    private final VehicleMapper mapper;
    private final VehicleSmartCreationService smartCreationService;

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
                .map(mapper::toResponse);
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
                .map(mapper::toResponse);
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
                .map(mapper::toResponse);
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
                .map(mapper::toResponse);
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
                .map(mapper::toResponse);
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
                .map(mapper::toResponse);
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
}