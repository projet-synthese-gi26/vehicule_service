package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;

public record ManufacturerRequest(
        @NotBlank(message = "Manufacturer name is required") String manufacturerName) {
}
