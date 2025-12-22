package com.yowyob.template.domain.ports.out;

import com.yowyob.template.domain.model.vehicle.details.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface VehicleDetailsRepositoryPort {
    // Amenity
    Mono<VehicleAmenity> saveAmenity(VehicleAmenity amenity);

    Flux<VehicleAmenity> findAmenitiesByVehicleId(UUID vehicleId);

    Mono<Void> deleteAmenity(UUID id);

    // CanTransport
    Mono<VehicleCanTransport> saveCanTransport(VehicleCanTransport canTransport);

    Flux<VehicleCanTransport> findCanTransportsByVehicleId(UUID vehicleId);

    Mono<Void> deleteCanTransport(UUID id);

    // IllustrationImage
    Mono<VehicleIllustrationImage> saveIllustrationImage(VehicleIllustrationImage image);

    Flux<VehicleIllustrationImage> findIllustrationImagesByVehicleId(UUID vehicleId);

    Mono<Void> deleteIllustrationImage(UUID id);

    // Keyword
    Mono<VehicleKeyword> saveKeyword(VehicleKeyword keyword);

    Flux<VehicleKeyword> findKeywordsByVehicleId(UUID vehicleId);

    Mono<Void> deleteKeyword(UUID id);

    // Review
    Mono<VehicleReview> saveReview(VehicleReview review);

    Flux<VehicleReview> findReviewsByVehicleId(UUID vehicleId);

    Mono<Void> deleteReview(UUID id);
}
