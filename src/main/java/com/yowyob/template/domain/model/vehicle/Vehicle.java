package com.yowyob.template.domain.model.vehicle;

import java.util.UUID;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public record Vehicle(
        UUID vehicleId,

        UUID vehicleMakeId,
        UUID vehicleModelId,
        UUID transmissionTypeId,
        UUID manufacturerId,
        UUID vehicleSizeId,
        UUID vehicleTypeId,
        UUID fuelTypeId,

        String vehicleSerialNumber,
        String vehicleSerialPhoto,
        String registrationNumber,
        String registrationPhoto,
        LocalDateTime registrationExpiryDate,

        BigDecimal tankCapacity,
        BigDecimal luggageMaxCapacity,
        Integer totalSeatNumber,

        BigDecimal averageFuelConsumptionPerKm,
        BigDecimal mileageAtStart,
        BigDecimal mileageSinceCommissioning,
        BigDecimal vehicleAgeAtStart,

        String brand,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
