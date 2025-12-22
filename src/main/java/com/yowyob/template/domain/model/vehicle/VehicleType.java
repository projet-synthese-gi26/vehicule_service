package com.yowyob.template.domain.model.vehicle;

import java.util.UUID;

public record VehicleType(
        UUID vehicleTypeId,
        String typeName) {
}
