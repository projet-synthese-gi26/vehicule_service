package com.yowyob.template.infrastructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SimplifiedVehicleRequest(
    
    // ==================================================================================
    // 1. CHAMPS OBLIGATOIRES (Références "Smart")
    // ==================================================================================
    
    @NotBlank(message = "La marque est obligatoire")
    @Schema(description = "Marque du véhicule", example = "Toyota", requiredMode = Schema.RequiredMode.REQUIRED)
    String makeName,

    @NotBlank(message = "Le modèle est obligatoire")
    @Schema(description = "Modèle du véhicule", example = "Corolla Hybride", requiredMode = Schema.RequiredMode.REQUIRED)
    String modelName,

    @NotBlank(message = "Le type de transmission est obligatoire")
    @Schema(description = "Type de transmission", example = "Automatique", requiredMode = Schema.RequiredMode.REQUIRED)
    String transmissionType,

    @NotBlank(message = "Le nom du fabricant est obligatoire")
    @Schema(description = "Fabricant (Usine d'assemblage ou Groupe)", example = "Toyota Tsutsumi Plant", requiredMode = Schema.RequiredMode.REQUIRED)
    String manufacturerName,

    @NotBlank(message = "La taille/gabarit est obligatoire")
    @Schema(description = "Taille/Gabarit du véhicule", example = "Berline Compacte", requiredMode = Schema.RequiredMode.REQUIRED)
    String sizeName,

    @NotBlank(message = "Le type d'usage est obligatoire")
    @Schema(description = "Usage/Type du véhicule", example = "Personnel", requiredMode = Schema.RequiredMode.REQUIRED)
    String typeName,

    @NotBlank(message = "Le type de carburant est obligatoire")
    @Schema(description = "Carburant", example = "Hybride Essence", requiredMode = Schema.RequiredMode.REQUIRED)
    String fuelTypeName,

    // ==================================================================================
    // 2. CHAMPS DÉTAILS (Peuvent être null)
    // ==================================================================================

    @Schema(description = "Numéro de série (VIN)", example = "SN-JW8292839182")
    String vehicleSerialNumber,

    @Schema(description = "URL de la photo du numéro de série", example = "https://cloud.storage.com/vehicles/vin/123.jpg")
    String vehicleSerialPhoto,

    @Schema(description = "Plaque d'immatriculation", example = "LT-458-XY")
    String registrationNumber,

    @Schema(description = "URL de la photo de la carte grise", example = "https://cloud.storage.com/vehicles/reg/123.jpg")
    String registrationPhoto,
    
    @Schema(description = "Date d'expiration de l'immatriculation (Format ISO)", example = "2028-12-31T23:59:59")
    LocalDateTime registrationExpiryDate,

    @Schema(description = "Capacité du réservoir (Litres ou kWh)", example = "45.5")
    BigDecimal tankCapacity,

    @Schema(description = "Capacité max des bagages (kg ou Litres)", example = "360")
    BigDecimal luggageMaxCapacity,

    @Schema(description = "Nombre total de sièges (y compris conducteur)", example = "5")
    Integer totalSeatNumber,

    @Schema(description = "Consommation moyenne (L/100km ou kWh/100km)", example = "4.2")
    BigDecimal averageFuelConsumptionPerKm,

    @Schema(description = "Kilométrage initial (à l'achat)", example = "10")
    BigDecimal mileageAtStart,

    @Schema(description = "Kilométrage actuel (depuis mise en service)", example = "15420.5")
    BigDecimal mileageSinceCommissioning,

    @Schema(description = "Âge du véhicule au début (années)", example = "0.5")
    BigDecimal vehicleAgeAtStart,

    @Schema(description = "Marque commerciale (souvent identique à la Marque)", example = "Toyota")
    String brand
) {}