package com.yowyob.template.infrastructure.adapters.outbound.persistence.repository;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.VehicleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface VehicleRepository extends ReactiveCrudRepository<VehicleEntity, UUID> {

    // Vérifie si le VIN existe déjà
    Mono<Boolean> existsByVehicleSerialNumber(String vehicleSerialNumber);

    // Vérifie si l'immatriculation existe déjà
    Mono<Boolean> existsByRegistrationNumber(String registrationNumber);

    // --- NOUVELLES MÉTHODES POUR L'UPDATE/PATCH ---

    // Vérifie si le VIN existe sur un AUTRE véhicule (ID différent)
    Mono<Boolean> existsByVehicleSerialNumberAndVehicleIdNot(String vehicleSerialNumber, UUID vehicleId);

    // Vérifie si l'immatriculation existe sur un AUTRE véhicule (ID différent)
    Mono<Boolean> existsByRegistrationNumberAndVehicleIdNot(String registrationNumber, UUID vehicleId);
}