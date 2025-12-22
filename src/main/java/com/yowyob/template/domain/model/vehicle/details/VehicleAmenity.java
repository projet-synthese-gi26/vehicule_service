package com.yowyob.template.domain.model.vehicle.details;

import java.util.UUID;

public record VehicleAmenity(
                UUID vehicleAmenityId,
                UUID vehicleId,
                String amenityName) {
}
