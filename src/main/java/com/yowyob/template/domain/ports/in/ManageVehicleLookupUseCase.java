package com.yowyob.template.domain.ports.in;

import com.yowyob.template.domain.model.vehicle.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ManageVehicleLookupUseCase {

    // FuelType
    Mono<FuelType> createFuelType(FuelType fuelType);

    Flux<FuelType> getAllFuelTypes();

    Mono<FuelType> getFuelTypeById(java.util.UUID id);

    Mono<Void> deleteFuelType(java.util.UUID id);

    // Manufacturer
    Mono<Manufacturer> createManufacturer(Manufacturer manufacturer);

    Flux<Manufacturer> getAllManufacturers();

    Mono<Manufacturer> getManufacturerById(java.util.UUID id);

    Mono<Void> deleteManufacturer(java.util.UUID id);

    // TransmissionType
    Mono<TransmissionType> createTransmissionType(TransmissionType transmissionType);

    Flux<TransmissionType> getAllTransmissionTypes();

    Mono<TransmissionType> getTransmissionTypeById(java.util.UUID id);

    Mono<Void> deleteTransmissionType(java.util.UUID id);

    // VehicleMake
    Mono<VehicleMake> createVehicleMake(VehicleMake vehicleMake);

    Flux<VehicleMake> getAllVehicleMakes();

    Mono<VehicleMake> getVehicleMakeById(java.util.UUID id);

    Mono<Void> deleteVehicleMake(java.util.UUID id);

    // VehicleModel
    Mono<VehicleModel> createVehicleModel(VehicleModel vehicleModel);

    Flux<VehicleModel> getAllVehicleModels();

    Mono<VehicleModel> getVehicleModelById(java.util.UUID id);

    Mono<Void> deleteVehicleModel(java.util.UUID id);

    // VehicleSize
    Mono<VehicleSize> createVehicleSize(VehicleSize vehicleSize);

    Flux<VehicleSize> getAllVehicleSizes();

    Mono<VehicleSize> getVehicleSizeById(java.util.UUID id);

    Mono<Void> deleteVehicleSize(java.util.UUID id);

    // VehicleType
    Mono<VehicleType> createVehicleType(VehicleType vehicleType);

    Flux<VehicleType> getAllVehicleTypes();

    Mono<VehicleType> getVehicleTypeById(java.util.UUID id);

    Mono<Void> deleteVehicleType(java.util.UUID id);
}
