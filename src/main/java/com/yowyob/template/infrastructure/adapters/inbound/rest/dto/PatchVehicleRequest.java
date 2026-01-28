package com.yowyob.template.infrastructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO spécifique pour le PATCH.
 * Tous les champs sont optionnels. Seuls les champs non-null seront mis à jour.
 */
public record PatchVehicleRequest(
    // Références (IDs)
    @Schema(description = "ID de la marque") UUID vehicleMakeId,
    @Schema(description = "ID du modèle") UUID vehicleModelId,
    @Schema(description = "ID de la transmission") UUID transmissionTypeId,
    @Schema(description = "ID du fabricant") UUID manufacturerId,
    @Schema(description = "ID de la taille") UUID vehicleSizeId,
    @Schema(description = "ID du type") UUID vehicleTypeId,
    @Schema(description = "ID du carburant") UUID fuelTypeId,

    // Infos textuelles
    @Schema(description = "Numéro de série") String vehicleSerialNumber,
    @Schema(description = "Photo Série") String vehicleSerialPhoto,
    @Schema(description = "Immatriculation") String registrationNumber,
    @Schema(description = "Photo Carte Grise") String registrationPhoto,
    @Schema(description = "Expiration Carte Grise") LocalDateTime registrationExpiryDate,

    // Données numériques
    BigDecimal tankCapacity,
    BigDecimal luggageMaxCapacity,
    Integer totalSeatNumber,
    BigDecimal averageFuelConsumptionPerKm,
    BigDecimal mileageAtStart,
    BigDecimal mileageSinceCommissioning,
    BigDecimal vehicleAgeAtStart,
    String brand
) {}