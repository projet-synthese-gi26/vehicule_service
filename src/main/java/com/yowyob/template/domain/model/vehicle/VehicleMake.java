package com.yowyob.template.domain.model.vehicle;

import java.util.UUID;

public record VehicleMake(
        UUID vehicleMakeId,
        String makeName) {
}
