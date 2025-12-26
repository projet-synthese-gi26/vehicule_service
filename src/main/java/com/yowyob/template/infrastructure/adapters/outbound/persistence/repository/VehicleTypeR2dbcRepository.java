package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleTypeEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VehicleTypeR2dbcRepository extends ReactiveCrudRepository<VehicleTypeEntity, UUID> {
    Mono<VehicleTypeEntity> findByTypeName(String typeName);
}
