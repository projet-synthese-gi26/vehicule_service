package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;

public record VehicleSizeRequest(
        @NotBlank(message = "Size name is required") String sizeName) {
}
