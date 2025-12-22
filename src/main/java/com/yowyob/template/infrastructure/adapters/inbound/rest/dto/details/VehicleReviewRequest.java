package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VehicleReviewRequest(
        @NotNull(message = "Vehicle ID is required") UUID vehicleId,
        @NotNull(message = "Review ID is required") UUID reviewId) {
}
