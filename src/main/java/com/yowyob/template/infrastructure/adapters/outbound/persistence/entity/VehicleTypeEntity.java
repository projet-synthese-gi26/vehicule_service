package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table(name = "VehicleType")
public class VehicleTypeEntity {

    @Id
    @Column("vehicle_type_id")
    private UUID vehicleTypeId;

    @Column("type_name")
    private String typeName;
}
