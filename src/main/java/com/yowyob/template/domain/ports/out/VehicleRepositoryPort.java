package com.yowyob.template.domain.ports.out;

import com.yowyob.template.domain.model.vehicle.Vehicle;

import reactor.core.publisher.Mono;

public interface VehicleRepositoryPort {

    Mono<Vehicle> saveVehicle(Vehicle vehicle);

}
