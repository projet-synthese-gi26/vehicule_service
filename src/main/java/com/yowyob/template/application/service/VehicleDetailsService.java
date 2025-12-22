package com.yowyob.template.application.service;

import com.yowyob.template.domain.model.vehicle.details.*;
import com.yowyob.template.domain.ports.in.ManageVehicleDetailsUseCase;
import com.yowyob.template.domain.ports.out.VehicleDetailsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleDetailsService implements ManageVehicleDetailsUseCase {

    private final VehicleDetailsRepositoryPort repositoryPort;

    // Amenity
    @Override
    public Mono<VehicleAmenity> createAmenity(VehicleAmenity amenity) {
        return repositoryPort.saveAmenity(amenity);
    }

    @Override
    public Flux<VehicleAmenity> getAmenitiesByVehicleId(UUID vehicleId) {
        return repositoryPort.findAmenitiesByVehicleId(vehicleId);
    }

    @Override
    public Mono<Void> deleteAmenity(UUID id) {
        return repositoryPort.deleteAmenity(id);
    }

    // CanTransport
    @Override
    public Mono<VehicleCanTransport> createCanTransport(VehicleCanTransport canTransport) {
        return repositoryPort.saveCanTransport(canTransport);
    }

    @Override
    public Flux<VehicleCanTransport> getCanTransportsByVehicleId(UUID vehicleId) {
        return repositoryPort.findCanTransportsByVehicleId(vehicleId);
    }

    @Override
    public Mono<Void> deleteCanTransport(UUID id) {
        return repositoryPort.deleteCanTransport(id);
    }

    // IllustrationImage
    @Override
    public Mono<VehicleIllustrationImage> createIllustrationImage(VehicleIllustrationImage image) {
        return repositoryPort.saveIllustrationImage(image);
    }

    @Override
    public Flux<VehicleIllustrationImage> getIllustrationImagesByVehicleId(UUID vehicleId) {
        return repositoryPort.findIllustrationImagesByVehicleId(vehicleId);
    }

    @Override
    public Mono<Void> deleteIllustrationImage(UUID id) {
        return repositoryPort.deleteIllustrationImage(id);
    }

    // Keyword
    @Override
    public Mono<VehicleKeyword> createKeyword(VehicleKeyword keyword) {
        return repositoryPort.saveKeyword(keyword);
    }

    @Override
    public Flux<VehicleKeyword> getKeywordsByVehicleId(UUID vehicleId) {
        return repositoryPort.findKeywordsByVehicleId(vehicleId);
    }

    @Override
    public Mono<Void> deleteKeyword(UUID id) {
        return repositoryPort.deleteKeyword(id);
    }

    // Review
    @Override
    public Mono<VehicleReview> createReview(VehicleReview review) {
        return repositoryPort.saveReview(review);
    }

    @Override
    public Flux<VehicleReview> getReviewsByVehicleId(UUID vehicleId) {
        return repositoryPort.findReviewsByVehicleId(vehicleId);
    }

    @Override
    public Mono<Void> deleteReview(UUID id) {
        return repositoryPort.deleteReview(id);
    }
}
