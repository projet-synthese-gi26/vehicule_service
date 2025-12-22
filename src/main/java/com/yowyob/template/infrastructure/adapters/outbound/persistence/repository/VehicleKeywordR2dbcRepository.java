package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleKeywordEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.util.UUID;

@Repository
public interface VehicleKeywordR2dbcRepository extends ReactiveCrudRepository<VehicleKeywordEntity, UUID> {
    Flux<VehicleKeywordEntity> findByVehicleId(UUID vehicleId);
}
