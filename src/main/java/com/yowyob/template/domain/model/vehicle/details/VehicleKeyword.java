package com.yowyob.template.domain.model.vehicle.details;

import java.util.UUID;

public record VehicleKeyword(
                UUID vehicleKeywordId,
                UUID vehicleId,
                String keyword) {
}