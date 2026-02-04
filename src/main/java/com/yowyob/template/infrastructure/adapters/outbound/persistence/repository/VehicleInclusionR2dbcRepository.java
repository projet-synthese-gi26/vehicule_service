package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleInclusionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface VehicleInclusionR2dbcRepository extends ReactiveCrudRepository<VehicleInclusionEntity, UUID> {
    Flux<VehicleInclusionEntity> findByVehicleId(UUID vehicleId);

    Mono<Void> deleteByVehicleId(UUID vehicleId);

    Mono<Void> deleteByVehicleIdAndInclusionName(UUID vehicleId, String inclusionName);
}
