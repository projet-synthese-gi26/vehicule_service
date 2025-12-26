package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleEntity;

import reactor.core.publisher.Flux;

@Repository
public interface VehicleR2dbcRepository extends ReactiveCrudRepository<VehicleEntity, UUID> {
    @Query("SELECT v.* FROM Vehicle v " +
           "INNER JOIN VehicleOwnership vo ON v.vehicle_id = vo.vehicle_id " +
           "WHERE vo.party_id = :partyId")
    Flux<VehicleEntity> findByPartyId(UUID partyId);

}
