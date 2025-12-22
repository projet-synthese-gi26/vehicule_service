package com.yowyob.template.domain.model.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransmissionType(
        UUID transmissionTypeId,
        String typeName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
