package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VehicleIllustrationImageRequest(
        @NotNull(message = "Vehicle ID is required") UUID vehicleId,
        @NotBlank(message = "Image path is required") String imagePath) {
}
