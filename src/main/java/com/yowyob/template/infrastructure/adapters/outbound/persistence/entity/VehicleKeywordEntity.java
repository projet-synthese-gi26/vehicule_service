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
@Table(name = "VehicleKeyword")
public class VehicleKeywordEntity {

    @Id
    @Column("vehicle_keyword_id")
    private UUID vehicleKeywordId;

    @Column("vehicle_id")
    private UUID vehicleId;

    @Column("keyword")
    private String keyword;
}
