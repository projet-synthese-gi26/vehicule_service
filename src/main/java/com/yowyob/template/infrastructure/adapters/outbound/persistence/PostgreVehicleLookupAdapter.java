package com.yowyob.template.infrastructure.adapters.outbound.persistence;

import com.yowyob.template.domain.model.vehicle.*;
import com.yowyob.template.domain.ports.out.VehicleLookupRepositoryPort;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.*;
import com.yowyob.template.infrastructure.mappers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostgreVehicleLookupAdapter implements VehicleLookupRepositoryPort {

    private final FuelTypeR2dbcRepository fuelTypeRepository;
    private final ManufacturerR2dbcRepository manufacturerRepository;
    private final TransmissionTypeR2dbcRepository transmissionTypeRepository;
    private final VehicleMakeR2dbcRepository vehicleMakeRepository;
    private final VehicleModelR2dbcRepository vehicleModelRepository;
    private final VehicleSizeR2dbcRepository vehicleSizeRepository;
    private final VehicleTypeR2dbcRepository vehicleTypeRepository;

    private final FuelTypeMapper fuelTypeMapper;
    private final ManufacturerMapper manufacturerMapper;
    private final TransmissionTypeMapper transmissionTypeMapper;
    private final VehicleMakeMapper vehicleMakeMapper;
    private final VehicleModelMapper vehicleModelMapper;
    private final VehicleSizeMapper vehicleSizeMapper;
    private final VehicleTypeMapper vehicleTypeMapper;

    // FuelType
    @Override
    public Mono<FuelType> saveFuelType(FuelType fuelType) {
        var entity = fuelTypeMapper.toEntity(fuelType);
        if (fuelType.fuelTypeId() != null) {
            entity.setFuelTypeId(fuelType.fuelTypeId());
        }
        return fuelTypeRepository.save(entity)
                .map(fuelTypeMapper::toDomain);
    }

    @Override
    public Flux<FuelType> findAllFuelTypes() {
        return fuelTypeRepository.findAll()
                .map(fuelTypeMapper::toDomain);
    }

    @Override
    public Mono<FuelType> findFuelTypeById(java.util.UUID id) {
        return fuelTypeRepository.findById(id)
                .map(fuelTypeMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteFuelType(java.util.UUID id) {
        return fuelTypeRepository.deleteById(id);
    }

    // Manufacturer
    @Override
    public Mono<Manufacturer> saveManufacturer(Manufacturer manufacturer) {
        var entity = manufacturerMapper.toEntity(manufacturer);
        if (manufacturer.manufacturerId() != null) {
            entity.setManufacturerId(manufacturer.manufacturerId());
        }
        return manufacturerRepository.save(entity)
                .map(manufacturerMapper::toDomain);
    }

    @Override
    public Flux<Manufacturer> findAllManufacturers() {
        return manufacturerRepository.findAll()
                .map(manufacturerMapper::toDomain);
    }

    @Override
    public Mono<Manufacturer> findManufacturerById(java.util.UUID id) {
        return manufacturerRepository.findById(id)
                .map(manufacturerMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteManufacturer(java.util.UUID id) {
        return manufacturerRepository.deleteById(id);
    }

    // TransmissionType
    @Override
    public Mono<TransmissionType> saveTransmissionType(TransmissionType transmissionType) {
        var entity = transmissionTypeMapper.toEntity(transmissionType);
        if (transmissionType.transmissionTypeId() != null) {
            entity.setTransmissionTypeId(transmissionType.transmissionTypeId());
        }
        return transmissionTypeRepository.save(entity)
                .map(transmissionTypeMapper::toDomain);
    }

    @Override
    public Flux<TransmissionType> findAllTransmissionTypes() {
        return transmissionTypeRepository.findAll()
                .map(transmissionTypeMapper::toDomain);
    }

    @Override
    public Mono<TransmissionType> findTransmissionTypeById(java.util.UUID id) {
        return transmissionTypeRepository.findById(id)
                .map(transmissionTypeMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteTransmissionType(java.util.UUID id) {
        return transmissionTypeRepository.deleteById(id);
    }

    // VehicleMake
    @Override
    public Mono<VehicleMake> saveVehicleMake(VehicleMake vehicleMake) {
        var entity = vehicleMakeMapper.toEntity(vehicleMake);
        if (vehicleMake.vehicleMakeId() != null) {
            entity.setVehicleMakeId(vehicleMake.vehicleMakeId());
        }
        return vehicleMakeRepository.save(entity)
                .map(vehicleMakeMapper::toDomain);
    }

    @Override
    public Flux<VehicleMake> findAllVehicleMakes() {
        return vehicleMakeRepository.findAll()
                .map(vehicleMakeMapper::toDomain);
    }

    @Override
    public Mono<VehicleMake> findVehicleMakeById(java.util.UUID id) {
        return vehicleMakeRepository.findById(id)
                .map(vehicleMakeMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteVehicleMake(java.util.UUID id) {
        return vehicleMakeRepository.deleteById(id);
    }

    // VehicleModel
    @Override
    public Mono<VehicleModel> saveVehicleModel(VehicleModel vehicleModel) {
        var entity = vehicleModelMapper.toEntity(vehicleModel);
        if (vehicleModel.vehicleModelId() != null) {
            entity.setVehicleModelId(vehicleModel.vehicleModelId());
        }
        return vehicleModelRepository.save(entity)
                .map(vehicleModelMapper::toDomain);
    }

    @Override
    public Flux<VehicleModel> findAllVehicleModels() {
        return vehicleModelRepository.findAll()
                .map(vehicleModelMapper::toDomain);
    }

    @Override
    public Mono<VehicleModel> findVehicleModelById(java.util.UUID id) {
        return vehicleModelRepository.findById(id)
                .map(vehicleModelMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteVehicleModel(java.util.UUID id) {
        return vehicleModelRepository.deleteById(id);
    }

    // VehicleSize
    @Override
    public Mono<VehicleSize> saveVehicleSize(VehicleSize vehicleSize) {
        var entity = vehicleSizeMapper.toEntity(vehicleSize);
        if (vehicleSize.vehicleSizeId() != null) {
            entity.setVehicleSizeId(vehicleSize.vehicleSizeId());
        }
        return vehicleSizeRepository.save(entity)
                .map(vehicleSizeMapper::toDomain);
    }

    @Override
    public Flux<VehicleSize> findAllVehicleSizes() {
        return vehicleSizeRepository.findAll()
                .map(vehicleSizeMapper::toDomain);
    }

    @Override
    public Mono<VehicleSize> findVehicleSizeById(java.util.UUID id) {
        return vehicleSizeRepository.findById(id)
                .map(vehicleSizeMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteVehicleSize(java.util.UUID id) {
        return vehicleSizeRepository.deleteById(id);
    }

    // VehicleType
    @Override
    public Mono<VehicleType> saveVehicleType(VehicleType vehicleType) {
        var entity = vehicleTypeMapper.toEntity(vehicleType);
        if (vehicleType.vehicleTypeId() != null) {
            entity.setVehicleTypeId(vehicleType.vehicleTypeId());
        }
        return vehicleTypeRepository.save(entity)
                .map(vehicleTypeMapper::toDomain);
    }

    @Override
    public Flux<VehicleType> findAllVehicleTypes() {
        return vehicleTypeRepository.findAll()
                .map(vehicleTypeMapper::toDomain);
    }

    @Override
    public Mono<VehicleType> findVehicleTypeById(java.util.UUID id) {
        return vehicleTypeRepository.findById(id)
                .map(vehicleTypeMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteVehicleType(java.util.UUID id) {
        return vehicleTypeRepository.deleteById(id);
    }
}
