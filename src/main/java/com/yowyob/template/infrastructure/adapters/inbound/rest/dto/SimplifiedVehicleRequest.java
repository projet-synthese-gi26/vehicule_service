package com.yowyob.template.infrastructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record SimplifiedVehicleRequest(
    
    @NotBlank
    @Schema(description = "Marque du véhicule", example = "Toyota", 
            allowableValues = {"Toyota", "Honda", "Ford", "Tesla", "BMW", "Mercedes"})
    String makeName,

    @NotBlank
    @Schema(description = "Modèle du véhicule", example = "Corolla", 
            allowableValues = {"Corolla", "Yaris", "Model 3", "X5", "Civic"})
    String modelName,

    @NotBlank
    @Schema(description = "Type de transmission", example = "Automatique", 
            allowableValues = {"Manuelle", "Automatique", "Séquentielle"})
    String transmissionType,

    @NotBlank
    @Schema(description = "Fabricant (Usine)", example = "Toyota Factory", 
            allowableValues = {"Toyota Factory", "Tesla Gigafactory", "BMW Group"})
    String manufacturerName,

    @NotBlank
    @Schema(description = "Taille/Gabarit", example = "Berline", 
            allowableValues = {"Compacte", "SUV", "Berline", "Utilitaire"})
    String sizeName,

    @NotBlank
    @Schema(description = "Usage/Type", example = "Personnel", 
            allowableValues = {"Personnel", "Commercial", "Transport"})
    String typeName,

    @NotBlank
    @Schema(description = "Carburant", example = "Essence", 
            allowableValues = {"Essence", "Diesel", "Electrique", "Hybride"})
    String fuelTypeName,

    // --- Les champs libres (Texte ou Nombre) ---

    @Schema(description = "Numéro de série (VIN)", example = "SN-123456789")
    String vehicleSerialNumber,

    @Schema(description = "URL de la photo du numéro de série", example = "http://image.com/serial.jpg")
    String vehicleSerialPhoto,

    @Schema(description = "Plaque d'immatriculation", example = "LT-001-AB")
    String registrationNumber,

    @Schema(description = "URL de la photo de la carte grise", example = "http://image.com/reg.jpg")
    String registrationPhoto,

    @Schema(description = "Capacité du réservoir (Litres ou kWh)", example = "50")
    BigDecimal tankCapacity,

    @Schema(description = "Capacité max des bagages (kg)", example = "400")
    BigDecimal luggageMaxCapacity,

    @Schema(description = "Nombre total de sièges", example = "5")
    Integer totalSeatNumber,

    @Schema(description = "Consommation moyenne (L/100km ou kWh/100km)", example = "5.5")
    BigDecimal averageFuelConsumptionPerKm,

    @Schema(description = "Kilométrage initial", example = "0")
    BigDecimal mileageAtStart,

    @Schema(description = "Kilométrage actuel", example = "100")
    BigDecimal mileageSinceCommissioning,

    @Schema(description = "Âge du véhicule au début (années)", example = "0")
    BigDecimal vehicleAgeAtStart,

    @Schema(description = "Marque commerciale (souvent idem que Make)", example = "Toyota")
    String brand
) {}