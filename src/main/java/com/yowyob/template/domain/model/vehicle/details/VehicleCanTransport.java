package com.yowyob.template.domain.model.vehicle.details;

import java.util.UUID;

public record VehicleCanTransport(
                UUID vehicleCanTransportId,
                UUID vehicleId,
                String item) {
}