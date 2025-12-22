package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import com.yowyob.template.domain.model.vehicle.ownership.UsageRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table(name = "VehicleOwnership")
public class VehicleOwnershipEntity {

    @Id
    @Column("vehicle_ownership_id")
    private UUID vehicleOwnershipId;

    @Column("vehicle_id")
    private UUID vehicleId;

    @Column("party_id")
    private UUID partyId;

    @Column("usage_role")
    private UsageRole usageRole;

    @Column("is_primary")
    private boolean isPrimary;

    @Column("valid_from")
    private LocalDateTime validFrom;

    @Column("valid_to")
    private LocalDateTime validTo;
}
