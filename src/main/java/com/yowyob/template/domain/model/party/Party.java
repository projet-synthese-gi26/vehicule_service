package com.yowyob.template.domain.model.party;

import java.util.UUID;

public record Party(
        UUID partyId,
        PartyType partyType,
        String displayName,
        String phone,
        String email) {
}