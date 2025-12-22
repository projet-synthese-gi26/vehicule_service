package com.yowyob.template.infrastructure.adapters.outbound.persistence;

import org.springframework.stereotype.Component;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.out.VehicleRepositoryPort;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.VehicleR2dbcRepository;
import com.yowyob.template.infrastructure.mappers.VehicleMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

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

}
