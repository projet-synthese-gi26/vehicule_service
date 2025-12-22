package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.VehicleMake;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleMakeRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleMakeResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleMakeEntity;

@Mapper(componentModel = "spring")
public interface VehicleMakeMapper {

    @Mapping(target = "vehicleMakeId", ignore = true)
    VehicleMake toDomain(VehicleMakeRequest request);

    VehicleMakeResponse toResponse(VehicleMake domain);

    @Mapping(target = "vehicleMakeId", ignore = true)
    VehicleMakeEntity toEntity(VehicleMake domain);

    VehicleMake toDomain(VehicleMakeEntity entity);
}
