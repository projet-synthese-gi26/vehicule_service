package com.yowyob.template.domain.ports.in;

import com.yowyob.template.domain.model.vehicle.details.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface ManageVehicleDetailsUseCase {
    // Amenity
    Mono<VehicleAmenity> createAmenity(VehicleAmenity amenity);

    Flux<VehicleAmenity> getAmenitiesByVehicleId(UUID vehicleId);

    Mono<Void> deleteAmenity(UUID id);

    // CanTransport
    Mono<VehicleCanTransport> createCanTransport(VehicleCanTransport canTransport);

    Flux<VehicleCanTransport> getCanTransportsByVehicleId(UUID vehicleId);

    Mono<Void> deleteCanTransport(UUID id);

    // IllustrationImage
    Mono<VehicleIllustrationImage> createIllustrationImage(VehicleIllustrationImage image);

    Flux<VehicleIllustrationImage> getIllustrationImagesByVehicleId(UUID vehicleId);

    Mono<Void> deleteIllustrationImage(UUID id);

    // Keyword
    Mono<VehicleKeyword> createKeyword(VehicleKeyword keyword);

    Flux<VehicleKeyword> getKeywordsByVehicleId(UUID vehicleId);

    Mono<Void> deleteKeyword(UUID id);

    // Review
    Mono<VehicleReview> createReview(VehicleReview review);

    Flux<VehicleReview> getReviewsByVehicleId(UUID vehicleId);

    Mono<Void> deleteReview(UUID id);
}
