package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleModelResponse(
        UUID vehicleModelId,
        String modelName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
