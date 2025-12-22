package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;

public record FuelTypeRequest(
        @NotBlank(message = "Fuel type name is required") String fuelTypeName) {
}
