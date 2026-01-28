package com.yowyob.template.application.service;

import com.yowyob.template.domain.exception.NotFoundException;
import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.domain.ports.out.VehicleRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Mono<Vehicle> updateVehicle(Vehicle vehicle) {
        UUID id = vehicle.vehicleId();
        return repositoryPort.findVehicleById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé avec l'ID : " + id)))
                .flatMap(existing -> validateUniqueness(vehicle.vehicleSerialNumber(), vehicle.registrationNumber(), id))
                .then(Mono.defer(() -> {
                    log.info("Mise à jour complète (PUT) du véhicule : {}", id);
                    return repositoryPort.saveVehicle(vehicle);
                }));
    }

    @Override
    @Transactional
    public Mono<Vehicle> patchVehicle(UUID id, Vehicle partial) {
        return repositoryPort.findVehicleById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé avec l'ID : " + id)))
                .flatMap(existing -> {
                    // Fusion manuelle
                    Vehicle merged = mergeVehicle(existing, partial);
                    
                    // Validation sur l'objet fusionné
                    return validateUniqueness(merged.vehicleSerialNumber(), merged.registrationNumber(), id)
                           .thenReturn(merged);
                })
                .flatMap(merged -> {
                    log.info("Mise à jour partielle (PATCH) du véhicule : {}", id);
                    return repositoryPort.saveVehicle(merged);
                });
    }

    // Méthode helper pour vérifier l'unicité avant update
    private Mono<Void> validateUniqueness(String serialNumber, String registrationNumber, UUID id) {
        return Mono.zip(
            repositoryPort.existsBySerialNumberAndIdNot(serialNumber, id),
            repositoryPort.existsByRegistrationNumberAndIdNot(registrationNumber, id)
        ).flatMap(tuple -> {
            boolean serialExists = tuple.getT1();
            boolean regExists = tuple.getT2();
            
            if (serialExists) {
                return Mono.error(new DuplicateKeyException("vehicle_serial_number exists"));
            }
            if (regExists) {
                return Mono.error(new DuplicateKeyException("registration_number exists"));
            }
            return Mono.empty();
        });
    }

    private Vehicle mergeVehicle(Vehicle existing, Vehicle partial) {
        return new Vehicle(
                existing.vehicleId(),
                partial.vehicleMakeId() != null ? partial.vehicleMakeId() : existing.vehicleMakeId(),
                partial.vehicleModelId() != null ? partial.vehicleModelId() : existing.vehicleModelId(),
                partial.transmissionTypeId() != null ? partial.transmissionTypeId() : existing.transmissionTypeId(),
                partial.manufacturerId() != null ? partial.manufacturerId() : existing.manufacturerId(),
                partial.vehicleSizeId() != null ? partial.vehicleSizeId() : existing.vehicleSizeId(),
                partial.vehicleTypeId() != null ? partial.vehicleTypeId() : existing.vehicleTypeId(),
                partial.fuelTypeId() != null ? partial.fuelTypeId() : existing.fuelTypeId(),
                
                partial.vehicleSerialNumber() != null ? partial.vehicleSerialNumber() : existing.vehicleSerialNumber(),
                partial.vehicleSerialPhoto() != null ? partial.vehicleSerialPhoto() : existing.vehicleSerialPhoto(),
                partial.registrationNumber() != null ? partial.registrationNumber() : existing.registrationNumber(),
                partial.registrationPhoto() != null ? partial.registrationPhoto() : existing.registrationPhoto(),
                partial.registrationExpiryDate() != null ? partial.registrationExpiryDate() : existing.registrationExpiryDate(),
                
                partial.tankCapacity() != null ? partial.tankCapacity() : existing.tankCapacity(),
                partial.luggageMaxCapacity() != null ? partial.luggageMaxCapacity() : existing.luggageMaxCapacity(),
                partial.totalSeatNumber() != null ? partial.totalSeatNumber() : existing.totalSeatNumber(),
                partial.averageFuelConsumptionPerKm() != null ? partial.averageFuelConsumptionPerKm() : existing.averageFuelConsumptionPerKm(),
                partial.mileageAtStart() != null ? partial.mileageAtStart() : existing.mileageAtStart(),
                partial.mileageSinceCommissioning() != null ? partial.mileageSinceCommissioning() : existing.mileageSinceCommissioning(),
                partial.vehicleAgeAtStart() != null ? partial.vehicleAgeAtStart() : existing.vehicleAgeAtStart(),
                
                partial.brand() != null ? partial.brand() : existing.brand(),
                existing.createdAt(),
                null 
        );
    }

    @Override
    public Flux<Vehicle> getAllVehicles() {
        return repositoryPort.findAllVehicles();
    }

    @Override
    public Flux<Vehicle> getVehiclesByOwner(UUID partyId) {
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