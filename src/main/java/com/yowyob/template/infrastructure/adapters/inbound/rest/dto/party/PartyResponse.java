package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.party;

import com.yowyob.template.domain.model.party.PartyType;

import java.util.UUID;

public record PartyResponse(
        UUID partyId,
        PartyType partyType,
        String displayName,
        String phone,
        String email) {
}
