package com.yowyob.template.infrastructure.adapters.outbound.persistence;

import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import com.yowyob.template.domain.ports.out.VehicleOwnershipRepositoryPort;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.VehicleOwnershipR2dbcRepository;
import com.yowyob.template.infrastructure.mappers.VehicleOwnershipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostgreVehicleOwnershipAdapter implements VehicleOwnershipRepositoryPort {

    private final VehicleOwnershipR2dbcRepository repository;
    private final VehicleOwnershipMapper mapper;

    @Override
    public Mono<VehicleOwnership> saveOwnership(VehicleOwnership ownership) {
        var entity = mapper.toEntity(ownership);
        if (ownership.vehicleOwnershipId() != null) {
            entity.setVehicleOwnershipId(ownership.vehicleOwnershipId());
        }
        return repository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleOwnership> findOwnershipsByVehicleId(UUID vehicleId) {
        return repository.findByVehicleId(vehicleId)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleOwnership> findOwnershipsByPartyId(UUID partyId) {
        return repository.findByPartyId(partyId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<VehicleOwnership> findOwnershipById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteOwnership(UUID id) {
        return repository.deleteById(id);
    }
}
