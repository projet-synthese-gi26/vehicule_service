//---> PATH: src/main/java/com/yowyob/template/infrastructure/adapters/inbound/rest/VehicleController.java
package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleResponse;
import com.yowyob.template.infrastructure.mappers.VehicleMapper;
import com.yowyob.template.infrastructure.security.AuthenticatedUser;
import com.yowyob.template.infrastructure.service.VehicleSmartCreationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final ManageVehicleUseCase manageVehicleUseCase;
    private final VehicleMapper mapper;
    private final VehicleSmartCreationService smartCreationService; // Injecter le nouveau service

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule", description = "Crée un nouveau véhicule dans le système.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public Mono<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest vehicleRequest) {
        return Mono.just(mapper.toDomain(vehicleRequest))
                .flatMap(manageVehicleUseCase::createVehicle)
                .map(mapper::toResponse);
    }

    @GetMapping
    @Operation(summary = "Lister mes véhicules", description = "Retourne uniquement les véhicules dont je suis propriétaire/utilisateur.")
    @SecurityRequirement(name = "bearerAuth")
    public Flux<VehicleResponse> getMyVehicles(@AuthenticationPrincipal AuthenticatedUser user) {
        // Sécurité défensive : Vérifier que l'utilisateur est bien injecté
        if (user == null || user.getId() == null) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non identifié"));
        }

        // On récupère l'ID depuis le token
        UUID partyId = UUID.fromString(user.getId());

        // On appelle le cas d'utilisation filtré par propriétaire
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

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un véhicule complet")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Véhicule mis à jour"),
            @ApiResponse(responseCode = "404", description = "Véhicule non trouvé")
    })
    public Mono<VehicleResponse> updateVehicle(
            @Parameter(description = "ID du véhicule à mettre à jour") @PathVariable UUID id,
            @Valid @RequestBody VehicleRequest vehicleRequest) {
        
        // On convertit le DTO en Domain
        Vehicle domainVehicle = mapper.toDomain(vehicleRequest);
        
        // On force l'ID du path sur l'objet domaine pour s'assurer qu'on met à jour le bon
        // Note: Comme 'Vehicle' est un Java Record (immuable), on doit recréer l'objet.
        Vehicle vehicleWithId = new Vehicle(
                id, // L'ID du path
                domainVehicle.vehicleMakeId(),
                domainVehicle.vehicleModelId(),
                domainVehicle.transmissionTypeId(),
                domainVehicle.manufacturerId(),
                domainVehicle.vehicleSizeId(),
                domainVehicle.vehicleTypeId(),
                domainVehicle.fuelTypeId(),
                domainVehicle.vehicleSerialNumber(),
                domainVehicle.vehicleSerialPhoto(),
                domainVehicle.registrationNumber(),
                domainVehicle.registrationPhoto(),
                domainVehicle.registrationExpiryDate(),
                domainVehicle.tankCapacity(),
                domainVehicle.luggageMaxCapacity(),
                domainVehicle.totalSeatNumber(),
                domainVehicle.averageFuelConsumptionPerKm(),
                domainVehicle.mileageAtStart(),
                domainVehicle.mileageSinceCommissioning(),
                domainVehicle.vehicleAgeAtStart(),
                domainVehicle.brand(),
                domainVehicle.createdAt(), // Gardé tel quel (géré par DB ou ignoré à l'update)
                domainVehicle.updatedAt()
        );

        return manageVehicleUseCase.updateVehicle(vehicleWithId)
                .map(mapper::toResponse);
    }

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


    

    @PostMapping("/simplified")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule (Mode Simplifié)", 
               description = "Utilise des listes déroulantes pour faciliter la sélection.")
    @SecurityRequirement(name = "bearerAuth")
    public Mono<VehicleResponse> createVehicleSimplified(
            // REMPLACEZ @Valid @RequestBody PAR @ParameterObject @Valid
            @ParameterObject @Valid SimplifiedVehicleRequest request) {
        
        return smartCreationService.createVehicleFromNames(request)
                .map(mapper::toResponse);
    }
}