package com.yowyob.template.application.service;

import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import com.yowyob.template.domain.ports.in.ManageVehicleOwnershipUseCase;
import com.yowyob.template.domain.ports.out.VehicleOwnershipRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleOwnershipService implements ManageVehicleOwnershipUseCase {

    private final VehicleOwnershipRepositoryPort repositoryPort;

    @Override
    public Mono<VehicleOwnership> createOwnership(VehicleOwnership ownership) {
        return repositoryPort.saveOwnership(ownership);
    }

    @Override
    public Flux<VehicleOwnership> getOwnershipsByVehicleId(UUID vehicleId) {
        return repositoryPort.findOwnershipsByVehicleId(vehicleId);
    }

    @Override
    public Flux<VehicleOwnership> getOwnershipsByPartyId(UUID partyId) {
        return repositoryPort.findOwnershipsByPartyId(partyId);
    }

    @Override
    public Mono<VehicleOwnership> getOwnershipById(UUID id) {
        return repositoryPort.findOwnershipById(id);
    }

    @Override
    public Mono<Void> deleteOwnership(UUID id) {
        return repositoryPort.deleteOwnership(id);
    }
}
