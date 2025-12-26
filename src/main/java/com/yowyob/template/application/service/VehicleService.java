//---> PATH: src/main/java/com/yowyob/template/application/service/VehicleService.java
package com.yowyob.template.application.service;

import com.yowyob.template.domain.exception.NotFoundException;
import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.domain.ports.out.VehicleRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService implements ManageVehicleUseCase {

    public final VehicleRepositoryPort repositoryPort;

    @Override
    public Mono<Vehicle> createVehicle(Vehicle vehicle) {
        log.info("Création du véhicule : {}", vehicle.registrationNumber());
        return repositoryPort.saveVehicle(vehicle);
    }

    @Override
    public Mono<Vehicle> updateVehicle(Vehicle vehicle) {
        return repositoryPort.findVehicleById(vehicle.vehicleId())
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé avec l'ID : " + vehicle.vehicleId())))
                .flatMap(existingVehicle -> {
                    // Ici, on sauvegarde directement car l'objet 'vehicle' contient déjà l'ID.
                    // R2DBC fera un update.
                    log.info("Mise à jour du véhicule : {}", vehicle.vehicleId());
                    return repositoryPort.saveVehicle(vehicle);
                });
    }

    @Override
    public Flux<Vehicle> getAllVehicles() {
        return repositoryPort.findAllVehicles();
    }
    @Override
    public Flux<Vehicle> getVehiclesByOwner(UUID partyId) {
        log.info("Récupération des véhicules pour le propriétaire : {}", partyId);
        return repositoryPort.findVehiclesByPartyId(partyId);
    }

    @Override
    public Mono<Vehicle> getVehicleById(UUID id) {
        return repositoryPort.findVehicleById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé avec l'ID : " + id)));
    }


    @Override
    public Mono<Void> deleteVehicle(UUID id) {
        return repositoryPort.findVehicleById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Impossible de supprimer. Véhicule non trouvé avec l'ID : " + id)))
                .flatMap(v -> {
                    log.info("Suppression du véhicule : {}", id);
                    return repositoryPort.deleteVehicle(id);
                });
    }
}