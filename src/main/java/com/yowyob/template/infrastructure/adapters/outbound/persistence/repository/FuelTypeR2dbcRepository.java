package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.FuelTypeEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FuelTypeR2dbcRepository extends ReactiveCrudRepository<FuelTypeEntity, UUID> {
    Mono<FuelTypeEntity> findByFuelTypeName(String fuelTypeName);
}
