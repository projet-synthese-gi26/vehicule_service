package com.yowyob.template.domain.model.vehicle.details;

import java.util.UUID;

public record VehicleInclusion(
        UUID vehicleInclusionId,
        UUID vehicleId,
        String inclusionName,
        Boolean isIncluded) {
}
