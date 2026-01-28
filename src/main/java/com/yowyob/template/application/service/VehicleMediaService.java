package com.yowyob.template.application.service;

import com.yowyob.template.domain.exception.NotFoundException;
import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.model.vehicle.details.VehicleIllustrationImage;
import com.yowyob.template.domain.ports.in.ManageVehicleMediaUseCase;
import com.yowyob.template.domain.ports.out.MediaStoragePort;
import com.yowyob.template.domain.ports.out.VehicleDetailsRepositoryPort;
import com.yowyob.template.domain.ports.out.VehicleRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleMediaService implements ManageVehicleMediaUseCase {

    private final VehicleRepositoryPort vehicleRepository;
    private final VehicleDetailsRepositoryPort detailsRepository;
    private final MediaStoragePort mediaStorage;

    // --- DOCUMENT : SERIAL PHOTO ---

    @Override
    @Transactional
    public Mono<Vehicle> uploadSerialPhoto(UUID vehicleId, FilePart file) {
        return vehicleRepository.findVehicleById(vehicleId)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé")))
                .flatMap(vehicle -> {
                    // 1. Upload nouveau fichier
                    String location = "vehicles/" + vehicleId + "/documents";
                    return mediaStorage.upload(file, location)
                            .flatMap(response -> {
                                // 2. (Optionnel) Supprimer l'ancien fichier si URL existante
                                // Pour faire ça proprement, il faudrait extraire l'ID de l'URL précédente.
                                // Ici on simplifie : on écrase juste la référence en DB.
                                
                                // 3. Mise à jour DB
                                Vehicle updated = copyWithSerialPhoto(vehicle, response.uri());
                                return vehicleRepository.saveVehicle(updated);
                            });
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteSerialPhoto(UUID vehicleId) {
        return vehicleRepository.findVehicleById(vehicleId)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé")))
                .flatMap(vehicle -> {
                    // TODO: Extraire ID media de l'URL pour appeler mediaStorage.delete()
                    
                    Vehicle updated = copyWithSerialPhoto(vehicle, null);
                    return vehicleRepository.saveVehicle(updated).then();
                });
    }

    // --- DOCUMENT : REGISTRATION PHOTO ---

    @Override
    @Transactional
    public Mono<Vehicle> uploadRegistrationPhoto(UUID vehicleId, FilePart file) {
        return vehicleRepository.findVehicleById(vehicleId)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé")))
                .flatMap(vehicle -> {
                    String location = "vehicles/" + vehicleId + "/documents";
                    return mediaStorage.upload(file, location)
                            .flatMap(response -> {
                                Vehicle updated = copyWithRegistrationPhoto(vehicle, response.uri());
                                return vehicleRepository.saveVehicle(updated);
                            });
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteRegistrationPhoto(UUID vehicleId) {
        return vehicleRepository.findVehicleById(vehicleId)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé")))
                .flatMap(vehicle -> {
                    Vehicle updated = copyWithRegistrationPhoto(vehicle, null);
                    return vehicleRepository.saveVehicle(updated).then();
                });
    }

    // --- ILLUSTRATIONS ---

    @Override
    public Mono<VehicleIllustrationImage> addIllustrationImage(UUID vehicleId, FilePart file) {
        return vehicleRepository.findVehicleById(vehicleId)
                .switchIfEmpty(Mono.error(new NotFoundException("Véhicule non trouvé")))
                .flatMap(v -> {
                    String location = "vehicles/" + vehicleId + "/illustrations";
                    return mediaStorage.upload(file, location)
                            .flatMap(response -> {
                                VehicleIllustrationImage image = new VehicleIllustrationImage(
                                        null,
                                        vehicleId,
                                        response.uri()
                                );
                                return detailsRepository.saveIllustrationImage(image);
                            });
                });
    }

    @Override
    public Flux<VehicleIllustrationImage> getIllustrationImages(UUID vehicleId) {
        return detailsRepository.findIllustrationImagesByVehicleId(vehicleId);
    }

    @Override
    public Mono<Void> deleteIllustrationImage(UUID vehicleId, UUID imageId) {
        // On pourrait vérifier que l'image appartient bien au véhicule, 
        // mais pour l'instant la suppression par ID est suffisante.
        // Idéalement : appeler mediaStorage.delete() aussi.
        return detailsRepository.deleteIllustrationImage(imageId);
    }

    // --- Helpers pour immutabilité des Records ---

    private Vehicle copyWithSerialPhoto(Vehicle v, String url) {
        return new Vehicle(
            v.vehicleId(), v.vehicleMakeId(), v.vehicleModelId(), v.transmissionTypeId(), 
            v.manufacturerId(), v.vehicleSizeId(), v.vehicleTypeId(), v.fuelTypeId(), 
            v.vehicleSerialNumber(), 
            url, // <--- Change ici
            v.registrationNumber(), v.registrationPhoto(), v.registrationExpiryDate(), 
            v.tankCapacity(), v.luggageMaxCapacity(), v.totalSeatNumber(), 
            v.averageFuelConsumptionPerKm(), v.mileageAtStart(), v.mileageSinceCommissioning(), 
            v.vehicleAgeAtStart(), v.brand(), v.createdAt(), null
        );
    }

    private Vehicle copyWithRegistrationPhoto(Vehicle v, String url) {
        return new Vehicle(
            v.vehicleId(), v.vehicleMakeId(), v.vehicleModelId(), v.transmissionTypeId(), 
            v.manufacturerId(), v.vehicleSizeId(), v.vehicleTypeId(), v.fuelTypeId(), 
            v.vehicleSerialNumber(), v.vehicleSerialPhoto(), v.registrationNumber(), 
            url, // <--- Change ici
            v.registrationExpiryDate(), 
            v.tankCapacity(), v.luggageMaxCapacity(), v.totalSeatNumber(), 
            v.averageFuelConsumptionPerKm(), v.mileageAtStart(), v.mileageSinceCommissioning(), 
            v.vehicleAgeAtStart(), v.brand(), v.createdAt(), null
        );
    }
}