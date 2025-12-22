package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import java.util.UUID;

public record VehicleKeywordResponse(
        UUID vehicleKeywordId,
        UUID vehicleId,
        String keyword) {
}
