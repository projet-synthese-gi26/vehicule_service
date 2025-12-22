package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VehicleKeywordRequest(
        @NotNull(message = "Vehicle ID is required") UUID vehicleId,
        @NotBlank(message = "Keyword is required") String keyword) {
}
