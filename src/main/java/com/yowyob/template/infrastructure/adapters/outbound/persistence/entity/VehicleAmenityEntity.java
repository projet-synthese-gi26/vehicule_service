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
@Table(name = "VehicleAmenity")
public class VehicleAmenityEntity {

    @Id
    @Column("vehicle_amenity_id")
    private UUID vehicleAmenityId;

    @Column("vehicle_id")
    private UUID vehicleId;

    @Column("amenity_name")
    private String amenityName;
}
