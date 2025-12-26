package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleModelEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VehicleModelR2dbcRepository extends ReactiveCrudRepository<VehicleModelEntity, UUID> {
    Mono<VehicleModelEntity> findByModelName(String modelName);
}
