package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import com.yowyob.template.domain.model.party.PartyType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table(name = "Party")
public class PartyEntity {

    @Id
    @Column("party_id")
    private UUID partyId;

    @Column("party_type")
    private PartyType partyType;

    @Column("display_name")
    private String displayName;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;
}
