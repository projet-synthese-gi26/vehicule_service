package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup;

import java.util.UUID;

public record VehicleTypeResponse(
        UUID vehicleTypeId,
        String typeName) {
}
