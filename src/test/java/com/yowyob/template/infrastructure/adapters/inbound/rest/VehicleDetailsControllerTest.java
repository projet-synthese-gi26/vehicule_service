package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.model.vehicle.details.*;
import com.yowyob.template.domain.ports.in.ManageVehicleDetailsUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.*;
import com.yowyob.template.infrastructure.mappers.VehicleDetailsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = VehicleDetailsController.class)
class VehicleDetailsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ManageVehicleDetailsUseCase detailsUseCase;

    @MockBean
    private VehicleDetailsMapper mapper;

    // --- Amenity Tests ---

    @Test
    void createAmenity() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        VehicleAmenityRequest request = new VehicleAmenityRequest(vehicleId, "AC");
        VehicleAmenity domain = new VehicleAmenity(null, vehicleId, "AC");
        VehicleAmenity savedDomain = new VehicleAmenity(id, vehicleId, "AC");
        VehicleAmenityResponse response = new VehicleAmenityResponse(id, vehicleId, "AC");

        when(mapper.toDomain(any(VehicleAmenityRequest.class))).thenReturn(domain);
        when(detailsUseCase.createAmenity(any(VehicleAmenity.class))).thenReturn(Mono.just(savedDomain));
        when(mapper.toResponse(any(VehicleAmenity.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/details/amenities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleAmenityResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getAmenitiesByVehicleId() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        VehicleAmenity domain = new VehicleAmenity(id, vehicleId, "AC");
        VehicleAmenityResponse response = new VehicleAmenityResponse(id, vehicleId, "AC");

        when(detailsUseCase.getAmenitiesByVehicleId(vehicleId)).thenReturn(Flux.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        webTestClient.get()
                .uri("/vehicles/details/amenities/vehicle/{vehicleId}", vehicleId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VehicleAmenityResponse.class)
                .contains(response);
    }

    // --- CanTransport Tests ---

    @Test
    void createCanTransport() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        VehicleCanTransportRequest request = new VehicleCanTransportRequest(vehicleId, "Bikes");
        VehicleCanTransport domain = new VehicleCanTransport(null, vehicleId, "Bikes");
        VehicleCanTransport savedDomain = new VehicleCanTransport(id, vehicleId, "Bikes");
        VehicleCanTransportResponse response = new VehicleCanTransportResponse(id, vehicleId, "Bikes");

        when(mapper.toDomain(any(VehicleCanTransportRequest.class))).thenReturn(domain);
        when(detailsUseCase.createCanTransport(any(VehicleCanTransport.class))).thenReturn(Mono.just(savedDomain));
        when(mapper.toResponse(any(VehicleCanTransport.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/details/can-transports")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleCanTransportResponse.class)
                .isEqualTo(response);
    }

    // --- IllustrationImage Tests ---

    @Test
    void createIllustrationImage() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        VehicleIllustrationImageRequest request = new VehicleIllustrationImageRequest(vehicleId, "/path/to/image.jpg");
        VehicleIllustrationImage domain = new VehicleIllustrationImage(null, vehicleId, "/path/to/image.jpg");
        VehicleIllustrationImage savedDomain = new VehicleIllustrationImage(id, vehicleId, "/path/to/image.jpg");
        VehicleIllustrationImageResponse response = new VehicleIllustrationImageResponse(id, vehicleId,
                "/path/to/image.jpg");

        when(mapper.toDomain(any(VehicleIllustrationImageRequest.class))).thenReturn(domain);
        when(detailsUseCase.createIllustrationImage(any(VehicleIllustrationImage.class)))
                .thenReturn(Mono.just(savedDomain));
        when(mapper.toResponse(any(VehicleIllustrationImage.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/details/illustration-images")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleIllustrationImageResponse.class)
                .isEqualTo(response);
    }

    // --- Keyword Tests ---

    @Test
    void createKeyword() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        VehicleKeywordRequest request = new VehicleKeywordRequest(vehicleId, "Fast");
        VehicleKeyword domain = new VehicleKeyword(null, vehicleId, "Fast");
        VehicleKeyword savedDomain = new VehicleKeyword(id, vehicleId, "Fast");
        VehicleKeywordResponse response = new VehicleKeywordResponse(id, vehicleId, "Fast");

        when(mapper.toDomain(any(VehicleKeywordRequest.class))).thenReturn(domain);
        when(detailsUseCase.createKeyword(any(VehicleKeyword.class))).thenReturn(Mono.just(savedDomain));
        when(mapper.toResponse(any(VehicleKeyword.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/details/keywords")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleKeywordResponse.class)
                .isEqualTo(response);
    }

    // --- Review Tests ---

    @Test
    void createReview() {
        UUID vehicleId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        VehicleReviewRequest request = new VehicleReviewRequest(vehicleId, reviewId);
        VehicleReview domain = new VehicleReview(vehicleId, reviewId);
        VehicleReviewResponse response = new VehicleReviewResponse(vehicleId, reviewId);

        when(mapper.toDomain(any(VehicleReviewRequest.class))).thenReturn(domain);
        when(detailsUseCase.createReview(any(VehicleReview.class))).thenReturn(Mono.just(domain));
        when(mapper.toResponse(any(VehicleReview.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/details/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleReviewResponse.class)
                .isEqualTo(response);
    }
}
