package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;

public record VehicleMakeRequest(
        @NotBlank(message = "Make name is required") String makeName) {
}
