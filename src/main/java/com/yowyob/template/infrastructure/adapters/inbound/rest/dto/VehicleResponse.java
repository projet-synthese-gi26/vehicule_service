package com.yowyob.template.infrastructure.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO utilisé pour renvoyer les informations d'un véhicule.
 * Contient tous les champs utiles pour l'affichage côté client.
 */
public record VehicleResponse(
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
