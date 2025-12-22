package com.yowyob.template.infrastructure.adapters.outbound.persistence;

import com.yowyob.template.domain.model.vehicle.details.*;
import com.yowyob.template.domain.ports.out.VehicleDetailsRepositoryPort;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.*;
import com.yowyob.template.infrastructure.mappers.VehicleDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostgreVehicleDetailsAdapter implements VehicleDetailsRepositoryPort {

    private final VehicleAmenityR2dbcRepository amenityRepository;
    private final VehicleCanTransportR2dbcRepository canTransportRepository;
    private final VehicleIllustrationImageR2dbcRepository illustrationImageRepository;
    private final VehicleKeywordR2dbcRepository keywordRepository;
    private final VehicleReviewR2dbcRepository reviewRepository;
    private final VehicleDetailsMapper mapper;

    // Amenity
    @Override
    public Mono<VehicleAmenity> saveAmenity(VehicleAmenity amenity) {
        var entity = mapper.toEntity(amenity);
        if (amenity.vehicleAmenityId() != null) {
            entity.setVehicleAmenityId(amenity.vehicleAmenityId());
        }
        return amenityRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleAmenity> findAmenitiesByVehicleId(UUID vehicleId) {
        return amenityRepository.findByVehicleId(vehicleId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteAmenity(UUID id) {
        return amenityRepository.deleteById(id);
    }

    // CanTransport
    @Override
    public Mono<VehicleCanTransport> saveCanTransport(VehicleCanTransport canTransport) {
        var entity = mapper.toEntity(canTransport);
        if (canTransport.vehicleCanTransportId() != null) {
            entity.setVehicleCanTransportId(canTransport.vehicleCanTransportId());
        }
        return canTransportRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleCanTransport> findCanTransportsByVehicleId(UUID vehicleId) {
        return canTransportRepository.findByVehicleId(vehicleId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteCanTransport(UUID id) {
        return canTransportRepository.deleteById(id);
    }

    // IllustrationImage
    @Override
    public Mono<VehicleIllustrationImage> saveIllustrationImage(VehicleIllustrationImage image) {
        var entity = mapper.toEntity(image);
        if (image.vehicleIllustrationImageId() != null) {
            entity.setVehicleIllustrationImageId(image.vehicleIllustrationImageId());
        }
        return illustrationImageRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleIllustrationImage> findIllustrationImagesByVehicleId(UUID vehicleId) {
        return illustrationImageRepository.findByVehicleId(vehicleId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteIllustrationImage(UUID id) {
        return illustrationImageRepository.deleteById(id);
    }

    // Keyword
    @Override
    public Mono<VehicleKeyword> saveKeyword(VehicleKeyword keyword) {
        var entity = mapper.toEntity(keyword);
        if (keyword.vehicleKeywordId() != null) {
            entity.setVehicleKeywordId(keyword.vehicleKeywordId());
        }
        return keywordRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleKeyword> findKeywordsByVehicleId(UUID vehicleId) {
        return keywordRepository.findByVehicleId(vehicleId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteKeyword(UUID id) {
        return keywordRepository.deleteById(id);
    }

    // Review
    @Override
    public Mono<VehicleReview> saveReview(VehicleReview review) {
        var entity = mapper.toEntity(review);
        if (review.reviewId() != null) {
            entity.setReviewId(review.reviewId());
        }
        return reviewRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<VehicleReview> findReviewsByVehicleId(UUID vehicleId) {
        return reviewRepository.findByVehicleId(vehicleId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteReview(UUID id) {
        return reviewRepository.deleteById(id);
    }
}
