package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table(name = "Manufacturer")
public class ManufacturerEntity {

    @Id
    @Column("manufacturer_id")
    private UUID manufacturerId;

    @Column("manufacturer_name")
    private String manufacturerName;
}
