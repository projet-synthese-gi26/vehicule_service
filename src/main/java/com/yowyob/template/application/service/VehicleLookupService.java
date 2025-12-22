package com.yowyob.template.application.service;

import com.yowyob.template.domain.model.vehicle.*;
import com.yowyob.template.domain.ports.in.ManageVehicleLookupUseCase;
import com.yowyob.template.domain.ports.out.VehicleLookupRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleLookupService implements ManageVehicleLookupUseCase {

    private final VehicleLookupRepositoryPort repositoryPort;

    // FuelType
    @Override
    public Mono<FuelType> createFuelType(FuelType fuelType) {
        return repositoryPort.saveFuelType(fuelType);
    }

    @Override
    public Flux<FuelType> getAllFuelTypes() {
        return repositoryPort.findAllFuelTypes();
    }

    @Override
    public Mono<FuelType> getFuelTypeById(UUID id) {
        return repositoryPort.findFuelTypeById(id);
    }

    @Override
    public Mono<Void> deleteFuelType(UUID id) {
        return repositoryPort.deleteFuelType(id);
    }

    // Manufacturer
    @Override
    public Mono<Manufacturer> createManufacturer(Manufacturer manufacturer) {
        return repositoryPort.saveManufacturer(manufacturer);
    }

    @Override
    public Flux<Manufacturer> getAllManufacturers() {
        return repositoryPort.findAllManufacturers();
    }

    @Override
    public Mono<Manufacturer> getManufacturerById(UUID id) {
        return repositoryPort.findManufacturerById(id);
    }

    @Override
    public Mono<Void> deleteManufacturer(UUID id) {
        return repositoryPort.deleteManufacturer(id);
    }

    // TransmissionType
    @Override
    public Mono<TransmissionType> createTransmissionType(TransmissionType transmissionType) {
        return repositoryPort.saveTransmissionType(transmissionType);
    }

    @Override
    public Flux<TransmissionType> getAllTransmissionTypes() {
        return repositoryPort.findAllTransmissionTypes();
    }

    @Override
    public Mono<TransmissionType> getTransmissionTypeById(UUID id) {
        return repositoryPort.findTransmissionTypeById(id);
    }

    @Override
    public Mono<Void> deleteTransmissionType(UUID id) {
        return repositoryPort.deleteTransmissionType(id);
    }

    // VehicleMake
    @Override
    public Mono<VehicleMake> createVehicleMake(VehicleMake vehicleMake) {
        return repositoryPort.saveVehicleMake(vehicleMake);
    }

    @Override
    public Flux<VehicleMake> getAllVehicleMakes() {
        return repositoryPort.findAllVehicleMakes();
    }

    @Override
    public Mono<VehicleMake> getVehicleMakeById(UUID id) {
        return repositoryPort.findVehicleMakeById(id);
    }

    @Override
    public Mono<Void> deleteVehicleMake(UUID id) {
        return repositoryPort.deleteVehicleMake(id);
    }

    // VehicleModel
    @Override
    public Mono<VehicleModel> createVehicleModel(VehicleModel vehicleModel) {
        return repositoryPort.saveVehicleModel(vehicleModel);
    }

    @Override
    public Flux<VehicleModel> getAllVehicleModels() {
        return repositoryPort.findAllVehicleModels();
    }

    @Override
    public Mono<VehicleModel> getVehicleModelById(UUID id) {
        return repositoryPort.findVehicleModelById(id);
    }

    @Override
    public Mono<Void> deleteVehicleModel(UUID id) {
        return repositoryPort.deleteVehicleModel(id);
    }

    // VehicleSize
    @Override
    public Mono<VehicleSize> createVehicleSize(VehicleSize vehicleSize) {
        return repositoryPort.saveVehicleSize(vehicleSize);
    }

    @Override
    public Flux<VehicleSize> getAllVehicleSizes() {
        return repositoryPort.findAllVehicleSizes();
    }

    @Override
    public Mono<VehicleSize> getVehicleSizeById(UUID id) {
        return repositoryPort.findVehicleSizeById(id);
    }

    @Override
    public Mono<Void> deleteVehicleSize(UUID id) {
        return repositoryPort.deleteVehicleSize(id);
    }

    // VehicleType
    @Override
    public Mono<VehicleType> createVehicleType(VehicleType vehicleType) {
        return repositoryPort.saveVehicleType(vehicleType);
    }

    @Override
    public Flux<VehicleType> getAllVehicleTypes() {
        return repositoryPort.findAllVehicleTypes();
    }

    @Override
    public Mono<VehicleType> getVehicleTypeById(UUID id) {
        return repositoryPort.findVehicleTypeById(id);
    }

    @Override
    public Mono<Void> deleteVehicleType(UUID id) {
        return repositoryPort.deleteVehicleType(id);
    }
}
