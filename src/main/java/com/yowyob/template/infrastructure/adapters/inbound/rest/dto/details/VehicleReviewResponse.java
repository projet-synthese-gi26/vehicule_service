package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details;

import java.util.UUID;

public record VehicleReviewResponse(
        UUID vehicleId,
        UUID reviewId) {
}
