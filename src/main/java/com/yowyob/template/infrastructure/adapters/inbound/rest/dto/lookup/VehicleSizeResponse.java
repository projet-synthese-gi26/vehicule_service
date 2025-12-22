package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleSizeResponse(
        UUID vehicleSizeId,
        String sizeName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
