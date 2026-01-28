package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleEntity;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "vehicleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "registrationExpiryDate", ignore = true)
    Vehicle toDomain(VehicleRequest request);

    VehicleResponse toResponse(Vehicle domain);
    @Mapping(target = "createdAt", ignore = true) // Créé par DB/Auditing
    @Mapping(target = "updatedAt", ignore = true) // Mise à jour automatique
    VehicleEntity toEntity(Vehicle domain);

    Vehicle toDomain(VehicleEntity entity);
}