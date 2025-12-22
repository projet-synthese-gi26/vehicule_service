package com.yowyob.template.domain.model.vehicle.details;

import java.util.UUID;

public record VehicleIllustrationImage(
                UUID vehicleIllustrationImageId,
                UUID vehicleId,
                String imagePath) {
}