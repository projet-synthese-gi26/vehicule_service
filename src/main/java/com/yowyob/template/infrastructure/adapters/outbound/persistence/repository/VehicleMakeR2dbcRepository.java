package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleMakeEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VehicleMakeR2dbcRepository extends ReactiveCrudRepository<VehicleMakeEntity, UUID> {
    Mono<VehicleMakeEntity> findByMakeName(String makeName);
}
