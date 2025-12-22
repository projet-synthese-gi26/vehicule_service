package com.yowyob.template.domain.ports.in;

import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface ManageVehicleOwnershipUseCase {
    Mono<VehicleOwnership> createOwnership(VehicleOwnership ownership);

    Flux<VehicleOwnership> getOwnershipsByVehicleId(UUID vehicleId);

    Flux<VehicleOwnership> getOwnershipsByPartyId(UUID partyId);

    Mono<VehicleOwnership> getOwnershipById(UUID id);

    Mono<Void> deleteOwnership(UUID id);
}
