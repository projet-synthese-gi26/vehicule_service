package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.TransmissionType;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.TransmissionTypeRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.TransmissionTypeResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.TransmissionTypeEntity;

@Mapper(componentModel = "spring")
public interface TransmissionTypeMapper {

    @Mapping(target = "transmissionTypeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TransmissionType toDomain(TransmissionTypeRequest request);

    TransmissionTypeResponse toResponse(TransmissionType domain);

    @Mapping(target = "transmissionTypeId", ignore = true)
    TransmissionTypeEntity toEntity(TransmissionType domain);

    TransmissionType toDomain(TransmissionTypeEntity entity);
}
