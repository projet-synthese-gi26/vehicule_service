package com.yowyob.template.infrastructure.adapters.outbound.persistence;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.out.VehicleRepositoryPort;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.VehicleR2dbcRepository;
import com.yowyob.template.infrastructure.mappers.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostgreVehicleAdapter implements VehicleRepositoryPort {

    private final VehicleR2dbcRepository repository;
    private final VehicleMapper mapper;

    @Override
    public Mono<Vehicle> saveVehicle(Vehicle vehicle) {
        return Mono.just(mapper.toEntity(vehicle))
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Vehicle> findAllVehicles() {
        return repository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Vehicle> findVehiclesByPartyId(UUID partyId) {
        return repository.findByPartyId(partyId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Vehicle> findVehicleById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteVehicle(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsBySerialNumberAndIdNot(String serialNumber, UUID id) {
        if (serialNumber == null) return Mono.just(false);
        return repository.existsByVehicleSerialNumberAndVehicleIdNot(serialNumber, id);
    }

    @Override
    public Mono<Boolean> existsByRegistrationNumberAndIdNot(String registrationNumber, UUID id) {
        if (registrationNumber == null) return Mono.just(false);
        return repository.existsByRegistrationNumberAndVehicleIdNot(registrationNumber, id);
    }
}