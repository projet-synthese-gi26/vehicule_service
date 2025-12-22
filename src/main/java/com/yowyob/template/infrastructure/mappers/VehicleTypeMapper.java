package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.VehicleType;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleTypeRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleTypeResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleTypeEntity;

@Mapper(componentModel = "spring")
public interface VehicleTypeMapper {

    @Mapping(target = "vehicleTypeId", ignore = true)
    VehicleType toDomain(VehicleTypeRequest request);

    VehicleTypeResponse toResponse(VehicleType domain);

    @Mapping(target = "vehicleTypeId", ignore = true)
    VehicleTypeEntity toEntity(VehicleType domain);

    VehicleType toDomain(VehicleTypeEntity entity);
}
