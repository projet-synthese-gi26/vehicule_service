package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import com.yowyob.template.domain.ports.in.ManageVehicleOwnershipUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership.VehicleOwnershipRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership.VehicleOwnershipResponse;
import com.yowyob.template.infrastructure.mappers.VehicleOwnershipMapper;
import com.yowyob.template.infrastructure.security.AuthenticatedUser;
import com.yowyob.template.domain.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles/ownerships")
@RequiredArgsConstructor
public class VehicleOwnershipController {

    private final ManageVehicleOwnershipUseCase ownershipUseCase;
    private final VehicleOwnershipMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Ownership", description = "Crée une ownership de véhicule. Le partyId est automatiquement récupéré depuis l'utilisateur authentifié.")
    @SecurityRequirement(name = "bearer-jwt")
    public Mono<VehicleOwnershipResponse> createOwnership(
            @Valid @RequestBody VehicleOwnershipRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        
        // Vérifier que l'utilisateur est authentifié
        if (authenticatedUser == null || authenticatedUser.getId() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));
        }
        
        // Récupérer le partyId depuis l'utilisateur authentifié
        UUID partyId = UUID.fromString(authenticatedUser.getId());
        
        // Mapper le request vers le domaine et ajouter le partyId
        VehicleOwnership ownership = new VehicleOwnership(
                null, // vehicleOwnershipId sera généré
                request.vehicleId(),
                partyId, // partyId récupéré automatiquement
                request.usageRole(),
                request.isPrimary(),
                request.validFrom(),
                request.validTo()
        );
        
        return ownershipUseCase.createOwnership(ownership)
                .map(mapper::toResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "Get My Ownerships", description = "Récupère les ownerships de l'utilisateur connecté")
    @SecurityRequirement(name = "bearer-jwt")
    public Flux<VehicleOwnershipResponse> getMyOwnerships(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null || authenticatedUser.getId() == null) {
            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));
        }
        UUID partyId = UUID.fromString(authenticatedUser.getId());
        return ownershipUseCase.getOwnershipsByPartyId(partyId)
                .map(mapper::toResponse);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get Ownerships by Vehicle ID")
    public Flux<VehicleOwnershipResponse> getOwnershipsByVehicleId(@PathVariable UUID vehicleId) {
        return ownershipUseCase.getOwnershipsByVehicleId(vehicleId)
                .map(mapper::toResponse);
    }

    @GetMapping("/party/{partyId}")
    @Operation(summary = "Get Ownerships by Party ID", description = "Pour les administrateurs uniquement")
    public Flux<VehicleOwnershipResponse> getOwnershipsByPartyId(@PathVariable UUID partyId) {
        return ownershipUseCase.getOwnershipsByPartyId(partyId)
                .map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Ownership by ID")
    public Mono<VehicleOwnershipResponse> getOwnershipById(@PathVariable UUID id) {
        return ownershipUseCase.getOwnershipById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Ownership not found with id: " + id)))
                .map(mapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Ownership")
    public Mono<Void> deleteOwnership(@PathVariable UUID id) {
        return ownershipUseCase.getOwnershipById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Ownership not found with id: " + id)))
                .flatMap(ownership -> ownershipUseCase.deleteOwnership(id));
    }
}
