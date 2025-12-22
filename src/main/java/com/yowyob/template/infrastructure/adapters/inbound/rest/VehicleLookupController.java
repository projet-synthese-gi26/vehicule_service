package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.ports.in.ManageVehicleLookupUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.lookup.*;
import com.yowyob.template.infrastructure.mappers.*;
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
@RequestMapping("/vehicles/lookup")
@RequiredArgsConstructor
public class VehicleLookupController {

    private final ManageVehicleLookupUseCase lookupUseCase;
    private final FuelTypeMapper fuelTypeMapper;
    private final ManufacturerMapper manufacturerMapper;
    private final TransmissionTypeMapper transmissionTypeMapper;
    private final VehicleMakeMapper vehicleMakeMapper;
    private final VehicleModelMapper vehicleModelMapper;
    private final VehicleSizeMapper vehicleSizeMapper;
    private final VehicleTypeMapper vehicleTypeMapper;

    // FuelType
    @PostMapping("/fuel-types")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Fuel Type")
    public Mono<FuelTypeResponse> createFuelType(@Valid @RequestBody FuelTypeRequest request) {
        return Mono.just(fuelTypeMapper.toDomain(request))
                .flatMap(lookupUseCase::createFuelType)
                .map(fuelTypeMapper::toResponse);
    }

    @GetMapping("/fuel-types")
    @Operation(summary = "Get All Fuel Types")
    public Flux<FuelTypeResponse> getAllFuelTypes() {
        return lookupUseCase.getAllFuelTypes()
                .map(fuelTypeMapper::toResponse);
    }

    @GetMapping("/fuel-types/{id}")
    @Operation(summary = "Get Fuel Type by ID")
    public Mono<FuelTypeResponse> getFuelTypeById(@PathVariable UUID id) {
        return lookupUseCase.getFuelTypeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Fuel Type not found with id: " + id)))
                .map(fuelTypeMapper::toResponse);
    }

    @DeleteMapping("/fuel-types/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Fuel Type")
    public Mono<Void> deleteFuelType(@PathVariable UUID id) {
        return lookupUseCase.getFuelTypeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Fuel Type not found with id: " + id)))
                .flatMap(fuelType -> lookupUseCase.deleteFuelType(id));
    }

    // Manufacturer
    @PostMapping("/manufacturers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Manufacturer")
    public Mono<ManufacturerResponse> createManufacturer(@Valid @RequestBody ManufacturerRequest request) {
        return Mono.just(manufacturerMapper.toDomain(request))
                .flatMap(lookupUseCase::createManufacturer)
                .map(manufacturerMapper::toResponse);
    }

    @GetMapping("/manufacturers")
    @Operation(summary = "Get All Manufacturers")
    public Flux<ManufacturerResponse> getAllManufacturers() {
        return lookupUseCase.getAllManufacturers()
                .map(manufacturerMapper::toResponse);
    }

    @GetMapping("/manufacturers/{id}")
    @Operation(summary = "Get Manufacturer by ID")
    public Mono<ManufacturerResponse> getManufacturerById(@PathVariable UUID id) {
        return lookupUseCase.getManufacturerById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Manufacturer not found with id: " + id)))
                .map(manufacturerMapper::toResponse);
    }

    @DeleteMapping("/manufacturers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Manufacturer")
    public Mono<Void> deleteManufacturer(@PathVariable UUID id) {
        return lookupUseCase.getManufacturerById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Manufacturer not found with id: " + id)))
                .flatMap(manufacturer -> lookupUseCase.deleteManufacturer(id));
    }

    // TransmissionType
    @PostMapping("/transmission-types")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Transmission Type")
    public Mono<TransmissionTypeResponse> createTransmissionType(@Valid @RequestBody TransmissionTypeRequest request) {
        return Mono.just(transmissionTypeMapper.toDomain(request))
                .flatMap(lookupUseCase::createTransmissionType)
                .map(transmissionTypeMapper::toResponse);
    }

    @GetMapping("/transmission-types")
    @Operation(summary = "Get All Transmission Types")
    public Flux<TransmissionTypeResponse> getAllTransmissionTypes() {
        return lookupUseCase.getAllTransmissionTypes()
                .map(transmissionTypeMapper::toResponse);
    }

    @GetMapping("/transmission-types/{id}")
    @Operation(summary = "Get Transmission Type by ID")
    public Mono<TransmissionTypeResponse> getTransmissionTypeById(@PathVariable UUID id) {
        return lookupUseCase.getTransmissionTypeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Transmission Type not found with id: " + id)))
                .map(transmissionTypeMapper::toResponse);
    }

    @DeleteMapping("/transmission-types/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Transmission Type")
    public Mono<Void> deleteTransmissionType(@PathVariable UUID id) {
        return lookupUseCase.getTransmissionTypeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Transmission Type not found with id: " + id)))
                .flatMap(transmissionType -> lookupUseCase.deleteTransmissionType(id));
    }

    // VehicleMake
    @PostMapping("/vehicle-makes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Make")
    public Mono<VehicleMakeResponse> createVehicleMake(@Valid @RequestBody VehicleMakeRequest request) {
        return Mono.just(vehicleMakeMapper.toDomain(request))
                .flatMap(lookupUseCase::createVehicleMake)
                .map(vehicleMakeMapper::toResponse);
    }

    @GetMapping("/vehicle-makes")
    @Operation(summary = "Get All Vehicle Makes")
    public Flux<VehicleMakeResponse> getAllVehicleMakes() {
        return lookupUseCase.getAllVehicleMakes()
                .map(vehicleMakeMapper::toResponse);
    }

    @GetMapping("/vehicle-makes/{id}")
    @Operation(summary = "Get Vehicle Make by ID")
    public Mono<VehicleMakeResponse> getVehicleMakeById(@PathVariable UUID id) {
        return lookupUseCase.getVehicleMakeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Make not found with id: " + id)))
                .map(vehicleMakeMapper::toResponse);
    }

    @DeleteMapping("/vehicle-makes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Vehicle Make")
    public Mono<Void> deleteVehicleMake(@PathVariable UUID id) {
        return lookupUseCase.getVehicleMakeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Make not found with id: " + id)))
                .flatMap(vehicleMake -> lookupUseCase.deleteVehicleMake(id));
    }

    // VehicleModel
    @PostMapping("/vehicle-models")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Model")
    public Mono<VehicleModelResponse> createVehicleModel(@Valid @RequestBody VehicleModelRequest request) {
        return Mono.just(vehicleModelMapper.toDomain(request))
                .flatMap(lookupUseCase::createVehicleModel)
                .map(vehicleModelMapper::toResponse);
    }

    @GetMapping("/vehicle-models")
    @Operation(summary = "Get All Vehicle Models")
    public Flux<VehicleModelResponse> getAllVehicleModels() {
        return lookupUseCase.getAllVehicleModels()
                .map(vehicleModelMapper::toResponse);
    }

    @GetMapping("/vehicle-models/{id}")
    @Operation(summary = "Get Vehicle Model by ID")
    public Mono<VehicleModelResponse> getVehicleModelById(@PathVariable UUID id) {
        return lookupUseCase.getVehicleModelById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Model not found with id: " + id)))
                .map(vehicleModelMapper::toResponse);
    }

    @DeleteMapping("/vehicle-models/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Vehicle Model")
    public Mono<Void> deleteVehicleModel(@PathVariable UUID id) {
        return lookupUseCase.getVehicleModelById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Model not found with id: " + id)))
                .flatMap(vehicleModel -> lookupUseCase.deleteVehicleModel(id));
    }

    // VehicleSize
    @PostMapping("/vehicle-sizes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Size")
    public Mono<VehicleSizeResponse> createVehicleSize(@Valid @RequestBody VehicleSizeRequest request) {
        return Mono.just(vehicleSizeMapper.toDomain(request))
                .flatMap(lookupUseCase::createVehicleSize)
                .map(vehicleSizeMapper::toResponse);
    }

    @GetMapping("/vehicle-sizes")
    @Operation(summary = "Get All Vehicle Sizes")
    public Flux<VehicleSizeResponse> getAllVehicleSizes() {
        return lookupUseCase.getAllVehicleSizes()
                .map(vehicleSizeMapper::toResponse);
    }

    @GetMapping("/vehicle-sizes/{id}")
    @Operation(summary = "Get Vehicle Size by ID")
    public Mono<VehicleSizeResponse> getVehicleSizeById(@PathVariable UUID id) {
        return lookupUseCase.getVehicleSizeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Size not found with id: " + id)))
                .map(vehicleSizeMapper::toResponse);
    }

    @DeleteMapping("/vehicle-sizes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Vehicle Size")
    public Mono<Void> deleteVehicleSize(@PathVariable UUID id) {
        return lookupUseCase.getVehicleSizeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Size not found with id: " + id)))
                .flatMap(vehicleSize -> lookupUseCase.deleteVehicleSize(id));
    }

    // VehicleType
    @PostMapping("/vehicle-types")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Vehicle Type")
    public Mono<VehicleTypeResponse> createVehicleType(@Valid @RequestBody VehicleTypeRequest request) {
        return Mono.just(vehicleTypeMapper.toDomain(request))
                .flatMap(lookupUseCase::createVehicleType)
                .map(vehicleTypeMapper::toResponse);
    }

    @GetMapping("/vehicle-types")
    @Operation(summary = "Get All Vehicle Types")
    public Flux<VehicleTypeResponse> getAllVehicleTypes() {
        return lookupUseCase.getAllVehicleTypes()
                .map(vehicleTypeMapper::toResponse);
    }

    @GetMapping("/vehicle-types/{id}")
    @Operation(summary = "Get Vehicle Type by ID")
    public Mono<VehicleTypeResponse> getVehicleTypeById(@PathVariable UUID id) {
        return lookupUseCase.getVehicleTypeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Type not found with id: " + id)))
                .map(vehicleTypeMapper::toResponse);
    }

    @DeleteMapping("/vehicle-types/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Vehicle Type")
    public Mono<Void> deleteVehicleType(@PathVariable UUID id) {
        return lookupUseCase.getVehicleTypeById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Type not found with id: " + id)))
                .flatMap(vehicleType -> lookupUseCase.deleteVehicleType(id));
    }
}
