package com.yowyob.template.domain.ports.in;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.model.vehicle.details.VehicleIllustrationImage;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ManageVehicleMediaUseCase {

    // --- Documents Uniques (1-1) ---
    Mono<Vehicle> uploadSerialPhoto(UUID vehicleId, FilePart file);
    Mono<Void> deleteSerialPhoto(UUID vehicleId);

    Mono<Vehicle> uploadRegistrationPhoto(UUID vehicleId, FilePart file);
    Mono<Void> deleteRegistrationPhoto(UUID vehicleId);

    // --- Illustrations (1-N) ---
    Mono<VehicleIllustrationImage> addIllustrationImage(UUID vehicleId, FilePart file);
    Flux<VehicleIllustrationImage> getIllustrationImages(UUID vehicleId);
    Mono<Void> deleteIllustrationImage(UUID vehicleId, UUID imageId); // vehicleId pour vérification sécu
}