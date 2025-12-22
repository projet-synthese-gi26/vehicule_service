package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.VehicleSize;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleSizeRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.VehicleSizeResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleSizeEntity;

@Mapper(componentModel = "spring")
public interface VehicleSizeMapper {

    @Mapping(target = "vehicleSizeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VehicleSize toDomain(VehicleSizeRequest request);

    VehicleSizeResponse toResponse(VehicleSize domain);

    @Mapping(target = "vehicleSizeId", ignore = true)
    VehicleSizeEntity toEntity(VehicleSize domain);

    VehicleSize toDomain(VehicleSizeEntity entity);

}