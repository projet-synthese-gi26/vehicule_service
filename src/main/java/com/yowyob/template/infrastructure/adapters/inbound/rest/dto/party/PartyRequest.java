package com.yowyob.template.infrastructure.adapters.inbound.rest.dto.party;

import com.yowyob.template.domain.model.party.PartyType;

public record PartyRequest(
        PartyType partyType,
        String displayName,
        String phone,
        String email) {
}
