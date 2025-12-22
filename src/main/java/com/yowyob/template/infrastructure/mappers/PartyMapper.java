package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yowyob.template.domain.model.party.Party;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.party.PartyRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.party.PartyResponse;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.PartyEntity;

@Mapper(componentModel = "spring")
public interface PartyMapper {

    @Mapping(target = "partyId", ignore = true)
    Party toDomain(PartyRequest request);

    PartyResponse toResponse(Party domain);

    @Mapping(target = "partyId", ignore = true)
    PartyEntity toEntity(Party domain);

    Party toDomain(PartyEntity entity);
}
