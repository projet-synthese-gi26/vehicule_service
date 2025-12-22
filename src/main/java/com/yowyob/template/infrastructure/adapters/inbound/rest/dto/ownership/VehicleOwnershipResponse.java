package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership;

import com.yowyob.template.domain.model.vehicle.ownership.UsageRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleOwnershipResponse(
        UUID vehicleOwnershipId,
        UUID vehicleId,
        UUID partyId,
        UsageRole usageRole,
        boolean isPrimary,
        LocalDateTime validFrom,
        LocalDateTime validTo) {
}
