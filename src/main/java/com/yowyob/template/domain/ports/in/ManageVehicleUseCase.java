//---> PATH: src/main/java/com/yowyob/template/domain/ports/in/ManageVehicleUseCase.java
package com.yowyob.template.domain.ports.in;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ManageVehicleUseCase {

    Mono<Vehicle> createVehicle(Vehicle vehicle);

    Mono<Vehicle> updateVehicle(Vehicle vehicle);

    Flux<Vehicle> getAllVehicles();

    Flux<Vehicle> getVehiclesByOwner(UUID partyId);

    Mono<Vehicle> getVehicleById(UUID id);

    Mono<Void> deleteVehicle(UUID id);
}