package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;

public record VehicleTypeRequest(
        @NotBlank(message = "Type name is required") String typeName) {
}
