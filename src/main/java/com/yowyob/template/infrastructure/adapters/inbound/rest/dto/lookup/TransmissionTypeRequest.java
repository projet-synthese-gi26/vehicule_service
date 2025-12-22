package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import jakarta.validation.constraints.NotBlank;

public record TransmissionTypeRequest(
        @NotBlank(message = "Transmission type name is required") String typeName) {
}
