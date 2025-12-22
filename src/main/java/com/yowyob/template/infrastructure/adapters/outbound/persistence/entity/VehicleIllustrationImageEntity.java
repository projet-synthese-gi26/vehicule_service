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
@Table(name = "VehicleIllustrationImage")
public class VehicleIllustrationImageEntity {

    @Id
    @Column("vehicle_illustration_image_id")
    private UUID vehicleIllustrationImageId;

    @Column("vehicle_id")
    private UUID vehicleId;

    @Column("image_path")
    private String imagePath;
}
