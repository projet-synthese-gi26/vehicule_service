package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.model.vehicle.ownership.UsageRole;
import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import com.yowyob.template.domain.ports.in.ManageVehicleOwnershipUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership.VehicleOwnershipRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.ownership.VehicleOwnershipResponse;
import com.yowyob.template.infrastructure.mappers.VehicleOwnershipMapper;
import com.yowyob.template.infrastructure.security.AuthenticatedUser;
import com.yowyob.template.infrastructure.security.JwtAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

@WebFluxTest(controllers = VehicleOwnershipController.class)
class VehicleOwnershipControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ManageVehicleOwnershipUseCase ownershipUseCase;

    @MockBean
    private VehicleOwnershipMapper mapper;

    private UUID partyId;
    private AuthenticatedUser authenticatedUser;
    private JwtAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        partyId = UUID.randomUUID();
        authenticatedUser = new AuthenticatedUser(
                partyId.toString(), 
                "testuser", 
                "test@example.com", 
                "+123456789",
                "Test", 
                "User", 
                List.of("USER"), 
                List.of()
        );
        authentication = new JwtAuthenticationToken(
                authenticatedUser, 
                "test-token", 
                authenticatedUser.getAuthorities()
        );
    }

    @Test
    void createOwnership() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Le nouveau request ne contient plus partyId
        VehicleOwnershipRequest request = new VehicleOwnershipRequest(vehicleId, UsageRole.OWNER, true, now, null);
        VehicleOwnership savedDomain = new VehicleOwnership(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);
        VehicleOwnershipResponse response = new VehicleOwnershipResponse(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);

        when(ownershipUseCase.createOwnership(any(VehicleOwnership.class))).thenReturn(Mono.just(savedDomain));
        when(mapper.toResponse(any(VehicleOwnership.class))).thenReturn(response);

        webTestClient
                .mutateWith(mockAuthentication(authentication))
                .post()
                .uri("/vehicles/ownerships")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleOwnershipResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getMyOwnerships() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        VehicleOwnership domain = new VehicleOwnership(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);
        VehicleOwnershipResponse response = new VehicleOwnershipResponse(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);

        when(ownershipUseCase.getOwnershipsByPartyId(partyId)).thenReturn(Flux.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        webTestClient
                .mutateWith(mockAuthentication(authentication))
                .get()
                .uri("/vehicles/ownerships/me")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VehicleOwnershipResponse.class)
                .contains(response);
    }

    @Test
    void getOwnershipsByVehicleId() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        VehicleOwnership domain = new VehicleOwnership(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);
        VehicleOwnershipResponse response = new VehicleOwnershipResponse(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);

        when(ownershipUseCase.getOwnershipsByVehicleId(vehicleId)).thenReturn(Flux.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        webTestClient
                .mutateWith(mockAuthentication(authentication))
                .get()
                .uri("/vehicles/ownerships/vehicle/{vehicleId}", vehicleId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VehicleOwnershipResponse.class)
                .contains(response);
    }

    @Test
    void getOwnershipsByPartyId() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        VehicleOwnership domain = new VehicleOwnership(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);
        VehicleOwnershipResponse response = new VehicleOwnershipResponse(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);

        when(ownershipUseCase.getOwnershipsByPartyId(partyId)).thenReturn(Flux.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        webTestClient
                .mutateWith(mockAuthentication(authentication))
                .get()
                .uri("/vehicles/ownerships/party/{partyId}", partyId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VehicleOwnershipResponse.class)
                .contains(response);
    }

    @Test
    void getOwnershipById() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        VehicleOwnership domain = new VehicleOwnership(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);
        VehicleOwnershipResponse response = new VehicleOwnershipResponse(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);

        when(ownershipUseCase.getOwnershipById(id)).thenReturn(Mono.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        webTestClient
                .mutateWith(mockAuthentication(authentication))
                .get()
                .uri("/vehicles/ownerships/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleOwnershipResponse.class)
                .isEqualTo(response);
    }

    @Test
    void deleteOwnership() {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        VehicleOwnership domain = new VehicleOwnership(id, vehicleId, partyId, UsageRole.OWNER, true, now, null);

        when(ownershipUseCase.getOwnershipById(id)).thenReturn(Mono.just(domain));
        when(ownershipUseCase.deleteOwnership(id)).thenReturn(Mono.empty());

        webTestClient
                .mutateWith(mockAuthentication(authentication))
                .delete()
                .uri("/vehicles/ownerships/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}
