package com.yowyob.template.infrastructure.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO utilisé pour créer un véhicule.
 * Les champs non fournis ici seront générés automatiquement (UUID, dates).
 */
public record VehicleRequest(
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
                BigDecimal tankCapacity,
                BigDecimal luggageMaxCapacity,
                Integer totalSeatNumber,
                BigDecimal averageFuelConsumptionPerKm,
                BigDecimal mileageAtStart,
                BigDecimal mileageSinceCommissioning,
                BigDecimal vehicleAgeAtStart,
                String brand) {
}
