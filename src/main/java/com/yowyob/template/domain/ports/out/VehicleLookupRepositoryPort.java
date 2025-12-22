package com.yowyob.template.domain.ports.out;

import com.yowyob.template.domain.model.vehicle.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleLookupRepositoryPort {

    // FuelType
    Mono<FuelType> saveFuelType(FuelType fuelType);

    Flux<FuelType> findAllFuelTypes();

    Mono<FuelType> findFuelTypeById(java.util.UUID id);

    Mono<Void> deleteFuelType(java.util.UUID id);

    // Manufacturer
    Mono<Manufacturer> saveManufacturer(Manufacturer manufacturer);

    Flux<Manufacturer> findAllManufacturers();

    Mono<Manufacturer> findManufacturerById(java.util.UUID id);

    Mono<Void> deleteManufacturer(java.util.UUID id);

    // TransmissionType
    Mono<TransmissionType> saveTransmissionType(TransmissionType transmissionType);

    Flux<TransmissionType> findAllTransmissionTypes();

    Mono<TransmissionType> findTransmissionTypeById(java.util.UUID id);

    Mono<Void> deleteTransmissionType(java.util.UUID id);

    // VehicleMake
    Mono<VehicleMake> saveVehicleMake(VehicleMake vehicleMake);

    Flux<VehicleMake> findAllVehicleMakes();

    Mono<VehicleMake> findVehicleMakeById(java.util.UUID id);

    Mono<Void> deleteVehicleMake(java.util.UUID id);

    // VehicleModel
    Mono<VehicleModel> saveVehicleModel(VehicleModel vehicleModel);

    Flux<VehicleModel> findAllVehicleModels();

    Mono<VehicleModel> findVehicleModelById(java.util.UUID id);

    Mono<Void> deleteVehicleModel(java.util.UUID id);

    // VehicleSize
    Mono<VehicleSize> saveVehicleSize(VehicleSize vehicleSize);

    Flux<VehicleSize> findAllVehicleSizes();

    Mono<VehicleSize> findVehicleSizeById(java.util.UUID id);

    Mono<Void> deleteVehicleSize(java.util.UUID id);

    // VehicleType
    Mono<VehicleType> saveVehicleType(VehicleType vehicleType);

    Flux<VehicleType> findAllVehicleTypes();

    Mono<VehicleType> findVehicleTypeById(java.util.UUID id);

    Mono<Void> deleteVehicleType(java.util.UUID id);
}
