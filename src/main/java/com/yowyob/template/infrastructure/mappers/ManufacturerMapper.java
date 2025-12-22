package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.Manufacturer;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.ManufacturerRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.ManufacturerResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.ManufacturerEntity;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    @Mapping(target = "manufacturerId", ignore = true)
    Manufacturer toDomain(ManufacturerRequest request);

    ManufacturerResponse toResponse(Manufacturer domain);

    @Mapping(target = "manufacturerId", ignore = true)
    ManufacturerEntity toEntity(Manufacturer domain);

    Manufacturer toDomain(ManufacturerEntity entity);
}
