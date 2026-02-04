package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VehicleInclusion")
public class VehicleInclusionEntity {

    @Id
    @Column("vehicle_inclusion_id")
    private UUID vehicleInclusionId;

    @Column("vehicle_id")
    private UUID vehicleId;

    @Column("inclusion_name")
    private String inclusionName;

    @Column("is_included")
    private Boolean isIncluded;
}
