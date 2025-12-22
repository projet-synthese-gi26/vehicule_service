package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership.VehicleOwnershipRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership.VehicleOwnershipResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleOwnershipEntity;

@Mapper(componentModel = "spring")
public interface VehicleOwnershipMapper {

    @Mapping(target = "vehicleOwnershipId", ignore = true)
    @Mapping(target = "partyId", ignore = true)
    VehicleOwnership toDomain(VehicleOwnershipRequest request);

    VehicleOwnershipResponse toResponse(VehicleOwnership domain);

    @Mapping(target = "vehicleOwnershipId", ignore = true)
    VehicleOwnershipEntity toEntity(VehicleOwnership domain);

    VehicleOwnership toDomain(VehicleOwnershipEntity entity);
}
