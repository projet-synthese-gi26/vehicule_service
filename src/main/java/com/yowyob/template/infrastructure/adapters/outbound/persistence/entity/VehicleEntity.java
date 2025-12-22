package com.yowyob.template.infrastructure.adapters.outbound.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table(name = "Vehicle")
public class VehicleEntity {

    @Id
    @Column("vehicle_id")
    private UUID vehicleId;

    // --- Foreign Keys ---
    @Column("vehicle_make_id")
    private UUID vehicleMakeId;

    @Column("vehicle_model_id")
    private UUID vehicleModelId;

    @Column("transmission_type_id")
    private UUID transmissionTypeId;

    @Column("manufacturer_id")
    private UUID manufacturerId;

    @Column("vehicle_size_id")
    private UUID vehicleSizeId;

    @Column("vehicle_type_id")
    private UUID vehicleTypeId;

    @Column("fuel_type_id")
    private UUID fuelTypeId;

    // --- Identification ---
    @Column("vehicle_serial_number")
    private String vehicleSerialNumber;

    @Column("vehicle_serial_photo")
    private String vehicleSerialPhoto;

    @Column("registration_number")
    private String registrationNumber;

    @Column("registration_photo")
    private String registrationPhoto;

    @Column("registration_expiry_date")
    private LocalDateTime registrationExpiryDate;

    // --- Technical Characteristics ---
    @Column("tank_capacity")
    private BigDecimal tankCapacity;

    @Column("luggage_max_capacity")
    private BigDecimal luggageMaxCapacity;

    @Column("total_seat_number")
    private Integer totalSeatNumber;

    // --- Usage / Consumption Data ---
    @Column("average_fuel_consumption_per_km")
    private BigDecimal averageFuelConsumptionPerKm;

    @Column("mileage_at_start")
    private BigDecimal mileageAtStart;

    @Column("mileage_since_commissioning")
    private BigDecimal mileageSinceCommissioning;

    @Column("vehicle_age_at_start")
    private BigDecimal vehicleAgeAtStart;

    // --- Metadata ---
    @Column("brand")
    private String brand;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
