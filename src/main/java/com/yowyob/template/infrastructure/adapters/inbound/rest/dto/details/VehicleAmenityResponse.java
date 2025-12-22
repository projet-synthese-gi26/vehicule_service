package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import java.util.UUID;

public record VehicleAmenityResponse(
        UUID vehicleAmenityId,
        UUID vehicleId,
        String amenityName) {
}
