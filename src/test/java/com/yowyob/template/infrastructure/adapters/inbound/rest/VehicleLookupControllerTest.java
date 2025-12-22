package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.model.vehicle.*;
import com.yowyob.template.domain.ports.in.ManageVehicleLookupUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.*;
import com.yowyob.template.infrastructure.mappers.*;
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

@WebFluxTest(controllers = VehicleLookupController.class)
class VehicleLookupControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ManageVehicleLookupUseCase lookupUseCase;

    @MockBean
    private FuelTypeMapper fuelTypeMapper;
    @MockBean
    private ManufacturerMapper manufacturerMapper;
    @MockBean
    private TransmissionTypeMapper transmissionTypeMapper;
    @MockBean
    private VehicleMakeMapper vehicleMakeMapper;
    @MockBean
    private VehicleModelMapper vehicleModelMapper;
    @MockBean
    private VehicleSizeMapper vehicleSizeMapper;
    @MockBean
    private VehicleTypeMapper vehicleTypeMapper;

    // --- FuelType Tests ---

    @Test
    void createFuelType() {
        UUID id = UUID.randomUUID();
        FuelTypeRequest request = new FuelTypeRequest("Diesel");
        FuelType domain = new FuelType(null, "Diesel");
        FuelType savedDomain = new FuelType(id, "Diesel");
        FuelTypeResponse response = new FuelTypeResponse(id, "Diesel");

        when(fuelTypeMapper.toDomain(any(FuelTypeRequest.class))).thenReturn(domain);
        when(lookupUseCase.createFuelType(any(FuelType.class))).thenReturn(Mono.just(savedDomain));
        when(fuelTypeMapper.toResponse(any(FuelType.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/fuel-types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FuelTypeResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getAllFuelTypes() {
        UUID id = UUID.randomUUID();
        FuelType domain = new FuelType(id, "Diesel");
        FuelTypeResponse response = new FuelTypeResponse(id, "Diesel");

        when(lookupUseCase.getAllFuelTypes()).thenReturn(Flux.just(domain));
        when(fuelTypeMapper.toResponse(domain)).thenReturn(response);

        webTestClient.get()
                .uri("/vehicles/lookup/fuel-types")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FuelTypeResponse.class)
                .contains(response);
    }

    @Test
    void getFuelTypeById() {
        UUID id = UUID.randomUUID();
        FuelType domain = new FuelType(id, "Diesel");
        FuelTypeResponse response = new FuelTypeResponse(id, "Diesel");

        when(lookupUseCase.getFuelTypeById(id)).thenReturn(Mono.just(domain));
        when(fuelTypeMapper.toResponse(domain)).thenReturn(response);

        webTestClient.get()
                .uri("/vehicles/lookup/fuel-types/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuelTypeResponse.class)
                .isEqualTo(response);
    }

    @Test
    void deleteFuelType() {
        UUID id = UUID.randomUUID();
        FuelType domain = new FuelType(id, "Diesel");
        when(lookupUseCase.getFuelTypeById(id)).thenReturn(Mono.just(domain));
        when(lookupUseCase.deleteFuelType(id)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/vehicles/lookup/fuel-types/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void getFuelTypeById_NotFound() {
        UUID id = UUID.randomUUID();
        when(lookupUseCase.getFuelTypeById(id)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/vehicles/lookup/fuel-types/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createFuelType_ValidationError() {
        FuelTypeRequest request = new FuelTypeRequest(""); // Invalid: blank name

        webTestClient.post()
                .uri("/vehicles/lookup/fuel-types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    // --- Manufacturer Tests ---

    @Test
    void createManufacturer() {
        UUID id = UUID.randomUUID();
        ManufacturerRequest request = new ManufacturerRequest("Toyota");
        Manufacturer domain = new Manufacturer(null, "Toyota");
        Manufacturer savedDomain = new Manufacturer(id, "Toyota");
        ManufacturerResponse response = new ManufacturerResponse(id, "Toyota");

        when(manufacturerMapper.toDomain(any(ManufacturerRequest.class))).thenReturn(domain);
        when(lookupUseCase.createManufacturer(any(Manufacturer.class))).thenReturn(Mono.just(savedDomain));
        when(manufacturerMapper.toResponse(any(Manufacturer.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ManufacturerResponse.class)
                .isEqualTo(response);
    }

    // --- TransmissionType Tests ---

    @Test
    void createTransmissionType() {
        UUID id = UUID.randomUUID();
        TransmissionTypeRequest request = new TransmissionTypeRequest("Automatic");
        TransmissionType domain = new TransmissionType(null, "Automatic", null, null);
        TransmissionType savedDomain = new TransmissionType(id, "Automatic", null, null);
        TransmissionTypeResponse response = new TransmissionTypeResponse(id, "Automatic", null, null);

        when(transmissionTypeMapper.toDomain(any(TransmissionTypeRequest.class))).thenReturn(domain);
        when(lookupUseCase.createTransmissionType(any(TransmissionType.class))).thenReturn(Mono.just(savedDomain));
        when(transmissionTypeMapper.toResponse(any(TransmissionType.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/transmission-types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TransmissionTypeResponse.class)
                .isEqualTo(response);
    }

    // --- VehicleMake Tests ---

    @Test
    void createVehicleMake() {
        UUID id = UUID.randomUUID();
        VehicleMakeRequest request = new VehicleMakeRequest("Corolla");
        VehicleMake domain = new VehicleMake(null, "Corolla");
        VehicleMake savedDomain = new VehicleMake(id, "Corolla");
        VehicleMakeResponse response = new VehicleMakeResponse(id, "Corolla");

        when(vehicleMakeMapper.toDomain(any(VehicleMakeRequest.class))).thenReturn(domain);
        when(lookupUseCase.createVehicleMake(any(VehicleMake.class))).thenReturn(Mono.just(savedDomain));
        when(vehicleMakeMapper.toResponse(any(VehicleMake.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/vehicle-makes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleMakeResponse.class)
                .isEqualTo(response);
    }

    // --- VehicleModel Tests ---

    @Test
    void createVehicleModel() {
        UUID id = UUID.randomUUID();
        UUID makeId = UUID.randomUUID();
        VehicleModelRequest request = new VehicleModelRequest(makeId, "2024");
        VehicleModel domain = new VehicleModel(null, "2024", null, null);
        VehicleModel savedDomain = new VehicleModel(id, "2024", null, null);
        VehicleModelResponse response = new VehicleModelResponse(id, "2024", null, null);

        when(vehicleModelMapper.toDomain(any(VehicleModelRequest.class))).thenReturn(domain);
        when(lookupUseCase.createVehicleModel(any(VehicleModel.class))).thenReturn(Mono.just(savedDomain));
        when(vehicleModelMapper.toResponse(any(VehicleModel.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/vehicle-models")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleModelResponse.class)
                .isEqualTo(response);
    }

    // --- VehicleSize Tests ---

    @Test
    void createVehicleSize() {
        UUID id = UUID.randomUUID();
        VehicleSizeRequest request = new VehicleSizeRequest("Compact");
        VehicleSize domain = new VehicleSize(null, "Compact", null, null);
        VehicleSize savedDomain = new VehicleSize(id, "Compact", null, null);
        VehicleSizeResponse response = new VehicleSizeResponse(id, "Compact", null, null);

        when(vehicleSizeMapper.toDomain(any(VehicleSizeRequest.class))).thenReturn(domain);
        when(lookupUseCase.createVehicleSize(any(VehicleSize.class))).thenReturn(Mono.just(savedDomain));
        when(vehicleSizeMapper.toResponse(any(VehicleSize.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/vehicle-sizes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleSizeResponse.class)
                .isEqualTo(response);
    }

    // --- VehicleType Tests ---

    @Test
    void createVehicleType() {
        UUID id = UUID.randomUUID();
        VehicleTypeRequest request = new VehicleTypeRequest("Sedan");
        VehicleType domain = new VehicleType(null, "Sedan");
        VehicleType savedDomain = new VehicleType(id, "Sedan");
        VehicleTypeResponse response = new VehicleTypeResponse(id, "Sedan");

        when(vehicleTypeMapper.toDomain(any(VehicleTypeRequest.class))).thenReturn(domain);
        when(lookupUseCase.createVehicleType(any(VehicleType.class))).thenReturn(Mono.just(savedDomain));
        when(vehicleTypeMapper.toResponse(any(VehicleType.class))).thenReturn(response);

        webTestClient.post()
                .uri("/vehicles/lookup/vehicle-types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(VehicleTypeResponse.class)
                .isEqualTo(response);
    }
}
