package com.yowyob.template.domain.model.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleSize(
        UUID vehicleSizeId,
        String sizeName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
