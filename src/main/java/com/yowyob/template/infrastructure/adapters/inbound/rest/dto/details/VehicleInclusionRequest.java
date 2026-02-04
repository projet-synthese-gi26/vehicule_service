package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VehicleInclusionRequest(
        @NotNull(message = "Vehicle ID is required") UUID vehicleId,
        @NotBlank(message = "Inclusion name is required") String inclusionName,
        @NotNull(message = "isIncluded is required") Boolean isIncluded) {
}
