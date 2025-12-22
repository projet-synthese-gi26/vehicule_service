package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleCanTransportEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.util.UUID;

@Repository
public interface VehicleCanTransportR2dbcRepository extends ReactiveCrudRepository<VehicleCanTransportEntity, UUID> {
    Flux<VehicleCanTransportEntity> findByVehicleId(UUID vehicleId);
}
