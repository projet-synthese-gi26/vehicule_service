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
@Table(name = "VehicleCanTransport")
public class VehicleCanTransportEntity {

    @Id
    @Column("vehicle_can_transport_id")
    private UUID vehicleCanTransportId;

    @Column("vehicle_id")
    private UUID vehicleId;

    @Column("item")
    private String item;
}
