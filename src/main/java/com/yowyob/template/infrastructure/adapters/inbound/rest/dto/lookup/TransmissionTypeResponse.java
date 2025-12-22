package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransmissionTypeResponse(
        UUID transmissionTypeId,
        String typeName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
