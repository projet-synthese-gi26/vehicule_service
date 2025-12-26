package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.ManufacturerEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ManufacturerR2dbcRepository extends ReactiveCrudRepository<ManufacturerEntity, UUID> {
    Mono<ManufacturerEntity> findByManufacturerName(String manufacturerName);
}

