package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table(name = "FuelType")
public class FuelTypeEntity {

    @Id
    @Column("fuel_type_id")
    private UUID fuelTypeId;

    @Column("fuel_type_name")
    private String fuelTypeName;
}
