package com.yowyob.template.domain.ports.in;

import com.yowyob.template.domain.model.vehicle.Vehicle;

import reactor.core.publisher.Mono;

public interface CreateVehicleUseCase {

    Mono<Vehicle> createVehicle(Vehicle vehicle);

}
