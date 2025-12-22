package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership;

import com.yowyob.template.domain.model.vehicle.ownership.UsageRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request DTO pour créer une ownership de véhicule.
 * Note: partyId est automatiquement récupéré depuis l'utilisateur authentifié.
 */
public record VehicleOwnershipRequest(
        @NotNull(message = "Vehicle ID is required") 
        @Schema(description = "ID du véhicule")
        UUID vehicleId,
        
        @NotNull(message = "Usage role is required") 
        @Schema(description = "Rôle d'utilisation (OWNER, DRIVER, etc.)")
        UsageRole usageRole,
        
        @Schema(description = "Indique si c'est l'ownership principal")
        boolean isPrimary,
        
        @Schema(description = "Date de début de validité")
        LocalDateTime validFrom,
        
        @Schema(description = "Date de fin de validité")
        LocalDateTime validTo) {
}
