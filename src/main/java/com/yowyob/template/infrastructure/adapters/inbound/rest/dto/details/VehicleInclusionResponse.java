package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import java.util.UUID;

public record VehicleInclusionResponse(
        UUID vehicleInclusionId,
        UUID vehicleId,
        String inclusionName,
        Boolean isIncluded) {
}
