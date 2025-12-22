package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import java.util.UUID;

public record VehicleCanTransportResponse(
        UUID vehicleCanTransportId,
        UUID vehicleId,
        String item) {
}
