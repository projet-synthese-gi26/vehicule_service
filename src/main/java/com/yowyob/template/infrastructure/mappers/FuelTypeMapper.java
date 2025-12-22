package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.FuelType;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.FuelTypeRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.FuelTypeResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.FuelTypeEntity;

@Mapper(componentModel = "spring")
public interface FuelTypeMapper {

    @Mapping(target = "fuelTypeId", ignore = true)
    FuelType toDomain(FuelTypeRequest request);

    FuelTypeResponse toResponse(FuelType domain);

    @Mapping(target = "fuelTypeId", ignore = true)
    FuelTypeEntity toEntity(FuelType domain);

    FuelType toDomain(FuelTypeEntity entity);
}
