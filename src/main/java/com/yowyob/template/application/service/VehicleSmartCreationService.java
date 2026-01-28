package com.yowyob.template.application.service;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.model.vehicle.ownership.UsageRole;
import com.yowyob.template.domain.model.vehicle.ownership.VehicleOwnership;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.domain.ports.out.VehicleOwnershipRepositoryPort;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.*;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleSmartCreationService {

    private final ManageVehicleUseCase manageVehicleUseCase;
    private final VehicleOwnershipRepositoryPort ownershipRepository;

    private final VehicleMakeR2dbcRepository makeRepo;
    private final VehicleModelR2dbcRepository modelRepo;
    private final TransmissionTypeR2dbcRepository transmissionRepo;
    private final ManufacturerR2dbcRepository manufacturerRepo;
    private final VehicleSizeR2dbcRepository sizeRepo;
    private final VehicleTypeR2dbcRepository typeRepo;
    private final FuelTypeR2dbcRepository fuelRepo;

    @Transactional
    public Mono<Vehicle> createVehicleFromNames(SimplifiedVehicleRequest request, UUID partyId) {
        log.info("Smart Creation: Début pour user {}", partyId);

        // 1. Get or Create avec génération d'UUID explicite en Java
        // Cela garantit que l'objet retourné A TOUJOURS un ID, même si la DB ne le renvoie pas
        
        Mono<VehicleMakeEntity> makeMono = makeRepo.findByMakeName(request.makeName())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return makeRepo.save(VehicleMakeEntity.builder().vehicleMakeId(id).makeName(request.makeName()).build());
                }));

        Mono<VehicleModelEntity> modelMono = modelRepo.findByModelName(request.modelName())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return modelRepo.save(VehicleModelEntity.builder().vehicleModelId(id).modelName(request.modelName()).build());
                }));

        Mono<TransmissionTypeEntity> transmissionMono = transmissionRepo.findByTypeName(request.transmissionType())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return transmissionRepo.save(TransmissionTypeEntity.builder().transmissionTypeId(id).typeName(request.transmissionType()).build());
                }));

        Mono<ManufacturerEntity> manufacturerMono = manufacturerRepo.findByManufacturerName(request.manufacturerName())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return manufacturerRepo.save(ManufacturerEntity.builder().manufacturerId(id).manufacturerName(request.manufacturerName()).build());
                }));

        Mono<VehicleSizeEntity> sizeMono = sizeRepo.findBySizeName(request.sizeName())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return sizeRepo.save(VehicleSizeEntity.builder().vehicleSizeId(id).sizeName(request.sizeName()).build());
                }));

        Mono<VehicleTypeEntity> typeMono = typeRepo.findByTypeName(request.typeName())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return typeRepo.save(VehicleTypeEntity.builder().vehicleTypeId(id).typeName(request.typeName()).build());
                }));

        Mono<FuelTypeEntity> fuelMono = fuelRepo.findByFuelTypeName(request.fuelTypeName())
                .switchIfEmpty(Mono.defer(() -> {
                    UUID id = UUID.randomUUID();
                    return fuelRepo.save(FuelTypeEntity.builder().fuelTypeId(id).fuelTypeName(request.fuelTypeName()).build());
                }));

        // 2. Exécution parallèle
        return Mono.zip(makeMono, modelMono, transmissionMono, manufacturerMono, sizeMono, typeMono, fuelMono)
            .flatMap(tuple -> {
                // Vérification de sécurité pour les logs
                log.info("IDs résolus - Make: {}, Model: {}", tuple.getT1().getVehicleMakeId(), tuple.getT2().getVehicleModelId());

                Vehicle vehicle = new Vehicle(
                    null, 
                    tuple.getT1().getVehicleMakeId(),
                    tuple.getT2().getVehicleModelId(),
                    tuple.getT3().getTransmissionTypeId(),
                    tuple.getT4().getManufacturerId(),
                    tuple.getT5().getVehicleSizeId(),
                    tuple.getT6().getVehicleTypeId(),
                    tuple.getT7().getFuelTypeId(),
                    request.vehicleSerialNumber(),
                    request.vehicleSerialPhoto(),
                    request.registrationNumber(),
                    request.registrationPhoto(),
                    request.registrationExpiryDate(),
                    request.tankCapacity(),
                    request.luggageMaxCapacity(),
                    request.totalSeatNumber(),
                    request.averageFuelConsumptionPerKm(),
                    request.mileageAtStart(),
                    request.mileageSinceCommissioning(),
                    request.vehicleAgeAtStart(),
                    request.brand(),
                    null, null
                );

                // 3. Gestion de l'Ownership et Sauvegarde
                return ownershipRepository.findOwnershipsByPartyId(partyId)
                        .hasElements()
                        .flatMap(hasExisting -> {
                            boolean isPrimary = !hasExisting;
                            
                            return manageVehicleUseCase.createVehicle(vehicle)
                                    .flatMap(savedVehicle -> {
                                        log.info("Véhicule créé avec ID: {}", savedVehicle.vehicleId());
                                        
                                        VehicleOwnership ownership = new VehicleOwnership(
                                                null,
                                                savedVehicle.vehicleId(),
                                                partyId,
                                                UsageRole.OWNER,
                                                isPrimary,
                                                LocalDateTime.now(),
                                                null
                                        );
                                        
                                        return ownershipRepository.saveOwnership(ownership)
                                                .doOnSuccess(o -> log.info("Ownership créé pour user {} et vehicle {}", partyId, savedVehicle.vehicleId()))
                                                .thenReturn(savedVehicle);
                                    });
                        });
            });
    }
}