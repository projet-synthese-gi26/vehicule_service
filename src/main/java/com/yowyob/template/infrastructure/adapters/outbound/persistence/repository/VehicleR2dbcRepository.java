package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleEntity;

@Repository
public interface VehicleR2dbcRepository extends ReactiveCrudRepository<VehicleEntity, UUID> {

}
