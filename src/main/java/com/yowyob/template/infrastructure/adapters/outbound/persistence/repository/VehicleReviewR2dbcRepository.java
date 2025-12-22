package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleReviewEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.util.UUID;

@Repository
public interface VehicleReviewR2dbcRepository extends ReactiveCrudRepository<VehicleReviewEntity, UUID> {
    Flux<VehicleReviewEntity> findByVehicleId(UUID vehicleId);
}
