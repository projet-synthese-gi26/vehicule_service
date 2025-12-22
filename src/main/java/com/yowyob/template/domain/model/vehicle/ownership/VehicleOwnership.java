package com.yowyob.template.domain.model.vehicle.ownership;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleOwnership(
        UUID vehicleOwnershipId,
        UUID vehicleId,
        UUID partyId,
        UsageRole usageRole,
        boolean isPrimary,
        LocalDateTime validFrom,
        LocalDateTime validTo) {
}