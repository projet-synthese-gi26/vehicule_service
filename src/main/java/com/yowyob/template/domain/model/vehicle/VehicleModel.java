package com.yowyob.template.domain.model.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleModel(
        UUID vehicleModelId,
        String modelName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
