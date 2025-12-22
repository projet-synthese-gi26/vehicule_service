package com.yowyob.template.domain.ports.out;

import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface VehicleOwnershipRepositoryPort {
    Mono<VehicleOwnership> saveOwnership(VehicleOwnership ownership);

    Flux<VehicleOwnership> findOwnershipsByVehicleId(UUID vehicleId);

    Flux<VehicleOwnership> findOwnershipsByPartyId(UUID partyId);

    Mono<VehicleOwnership> findOwnershipById(UUID id);

    Mono<Void> deleteOwnership(UUID id);
}
