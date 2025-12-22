package com.yowyob.template.infrastructure.mappers;

import com.yowyob.template.domain.model.vehicle.VehicleModel;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleModelEntity;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleModelRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleModelResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleModelMapper {

    @Mapping(target = "vehicleModelId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VehicleModel toDomain(VehicleModelRequest request);

    VehicleModelResponse toResponse(VehicleModel domain);

    @Mapping(target = "vehicleModelId", ignore = true)
    VehicleModelEntity toEntity(VehicleModel domain);

    VehicleModel toDomain(VehicleModelEntity entity);
}
