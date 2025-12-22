package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleAmenityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.util.UUID;

@Repository
public interface VehicleAmenityR2dbcRepository extends ReactiveCrudRepository<VehicleAmenityEntity, UUID> {
    Flux<VehicleAmenityEntity> findByVehicleId(UUID vehicleId);
}
