package com.yowyob.template.domain.ports.out;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VehicleRepositoryPort {

    Mono<Vehicle> saveVehicle(Vehicle vehicle);

    Flux<Vehicle> findAllVehicles();

    Flux<Vehicle> findVehiclesByPartyId(UUID partyId);

    Mono<Vehicle> findVehicleById(UUID id);

    Mono<Void> deleteVehicle(UUID id);

    // --- Validation d'unicité pour Update/Patch ---
    Mono<Boolean> existsBySerialNumberAndIdNot(String serialNumber, UUID id);
    Mono<Boolean> existsByRegistrationNumberAndIdNot(String registrationNumber, UUID id);
}