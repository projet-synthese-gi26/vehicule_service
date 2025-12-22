package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.ports.in.ManageVehicleDetailsUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.*;
import com.yowyob.template.infrastructure.mappers.VehicleDetailsMapper;
import com.yowyob.template.domain.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles/details")
@RequiredArgsConstructor
public class VehicleDetailsController {

    private final ManageVehicleDetailsUseCase detailsUseCase;
    private final VehicleDetailsMapper mapper;

    // Amenity
    @PostMapping("/amenities")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Amenity")
    public Mono<VehicleAmenityResponse> createAmenity(@Valid @RequestBody VehicleAmenityRequest request) {
        return Mono.just(mapper.toDomain(request))
                .flatMap(detailsUseCase::createAmenity)
                .map(mapper::toResponse);
    }

    @GetMapping("/amenities/vehicle/{vehicleId}")
    @Operation(summary = "Get Amenities by Vehicle ID")
    public Flux<VehicleAmenityResponse> getAmenitiesByVehicleId(@PathVariable UUID vehicleId) {
        return detailsUseCase.getAmenitiesByVehicleId(vehicleId)
                .map(mapper::toResponse);
    }

    @DeleteMapping("/amenities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Amenity")
    public Mono<Void> deleteAmenity(@PathVariable UUID id) {
        return detailsUseCase.deleteAmenity(id);
    }

    // CanTransport
    @PostMapping("/can-transports")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Can Transport")
    public Mono<VehicleCanTransportResponse> createCanTransport(
            @Valid @RequestBody VehicleCanTransportRequest request) {
        return Mono.just(mapper.toDomain(request))
                .flatMap(detailsUseCase::createCanTransport)
                .map(mapper::toResponse);
    }

    @GetMapping("/can-transports/vehicle/{vehicleId}")
    @Operation(summary = "Get Can Transports by Vehicle ID")
    public Flux<VehicleCanTransportResponse> getCanTransportsByVehicleId(@PathVariable UUID vehicleId) {
        return detailsUseCase.getCanTransportsByVehicleId(vehicleId)
                .map(mapper::toResponse);
    }

    @DeleteMapping("/can-transports/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Can Transport")
    public Mono<Void> deleteCanTransport(@PathVariable UUID id) {
        return detailsUseCase.deleteCanTransport(id);
    }

    // IllustrationImage
    @PostMapping("/illustration-images")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Illustration Image")
    public Mono<VehicleIllustrationImageResponse> createIllustrationImage(
            @Valid @RequestBody VehicleIllustrationImageRequest request) {
        return Mono.just(mapper.toDomain(request))
                .flatMap(detailsUseCase::createIllustrationImage)
                .map(mapper::toResponse);
    }

    @GetMapping("/illustration-images/vehicle/{vehicleId}")
    @Operation(summary = "Get Illustration Images by Vehicle ID")
    public Flux<VehicleIllustrationImageResponse> getIllustrationImagesByVehicleId(@PathVariable UUID vehicleId) {
        return detailsUseCase.getIllustrationImagesByVehicleId(vehicleId)
                .map(mapper::toResponse);
    }

    @DeleteMapping("/illustration-images/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Illustration Image")
    public Mono<Void> deleteIllustrationImage(@PathVariable UUID id) {
        return detailsUseCase.deleteIllustrationImage(id);
    }

    // Keyword
    @PostMapping("/keywords")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Keyword")
    public Mono<VehicleKeywordResponse> createKeyword(@Valid @RequestBody VehicleKeywordRequest request) {
        return Mono.just(mapper.toDomain(request))
                .flatMap(detailsUseCase::createKeyword)
                .map(mapper::toResponse);
    }

    @GetMapping("/keywords/vehicle/{vehicleId}")
    @Operation(summary = "Get Keywords by Vehicle ID")
    public Flux<VehicleKeywordResponse> getKeywordsByVehicleId(@PathVariable UUID vehicleId) {
        return detailsUseCase.getKeywordsByVehicleId(vehicleId)
                .map(mapper::toResponse);
    }

    @DeleteMapping("/keywords/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Keyword")
    public Mono<Void> deleteKeyword(@PathVariable UUID id) {
        return detailsUseCase.deleteKeyword(id);
    }

    // Review
    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Review")
    public Mono<VehicleReviewResponse> createReview(@Valid @RequestBody VehicleReviewRequest request) {
        return Mono.just(mapper.toDomain(request))
                .flatMap(detailsUseCase::createReview)
                .map(mapper::toResponse);
    }

    @GetMapping("/reviews/vehicle/{vehicleId}")
    @Operation(summary = "Get Reviews by Vehicle ID")
    public Flux<VehicleReviewResponse> getReviewsByVehicleId(@PathVariable UUID vehicleId) {
        return detailsUseCase.getReviewsByVehicleId(vehicleId)
                .map(mapper::toResponse);
    }

    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Review")
    public Mono<Void> deleteReview(@PathVariable UUID id) {
        return detailsUseCase.deleteReview(id);
    }
}
