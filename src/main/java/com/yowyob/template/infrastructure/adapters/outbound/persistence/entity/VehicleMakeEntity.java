package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table(name = "VehicleMake")
public class VehicleMakeEntity {

    @Id
    @Column("vehicle_make_id")
    private UUID vehicleMakeId;

    @Column("make_name")
    private String makeName;
}
