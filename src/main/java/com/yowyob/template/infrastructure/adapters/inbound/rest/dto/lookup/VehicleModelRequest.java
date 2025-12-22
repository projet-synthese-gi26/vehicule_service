package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VehicleModelRequest(
        @NotNull(message = "Vehicle make ID is required") UUID vehicleMakeId,
        @NotBlank(message = "Model name is required") String modelName) {
}
