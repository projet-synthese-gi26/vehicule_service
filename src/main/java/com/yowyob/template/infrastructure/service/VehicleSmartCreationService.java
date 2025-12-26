package com.yowyob.template.infrastructure.service;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.ManageVehicleUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;
import com.yowyob.template.infrastructure.adapters.outbound.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.SimplifiedVehicleRequest;

@Service
@RequiredArgsConstructor
public class VehicleSmartCreationService {

    private final ManageVehicleUseCase manageVehicleUseCase;
    
    // Repositories pour faire les lookups
    private final VehicleMakeR2dbcRepository makeRepo;
    private final VehicleModelR2dbcRepository modelRepo;
    private final TransmissionTypeR2dbcRepository transmissionRepo;
    private final ManufacturerR2dbcRepository manufacturerRepo;
    private final VehicleSizeR2dbcRepository sizeRepo;
    private final VehicleTypeR2dbcRepository typeRepo;
    private final FuelTypeR2dbcRepository fuelRepo;

    public Mono<Vehicle> createVehicleFromNames(SimplifiedVehicleRequest request) {
        
        // On lance tous les lookups en parallèle avec Mono.zip
        return Mono.zip(
            makeRepo.findByMakeName(request.makeName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marque inconnue: " + request.makeName()))),
            
            modelRepo.findByModelName(request.modelName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modèle inconnu: " + request.modelName()))),
            
            transmissionRepo.findByTypeName(request.transmissionType())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transmission inconnue: " + request.transmissionType()))),
            
            manufacturerRepo.findByManufacturerName(request.manufacturerName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fabricant inconnu: " + request.manufacturerName()))),
            
            sizeRepo.findBySizeName(request.sizeName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Taille inconnue: " + request.sizeName()))),
            
            typeRepo.findByTypeName(request.typeName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type véhicule inconnu: " + request.typeName()))),
            
            fuelRepo.findByFuelTypeName(request.fuelTypeName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carburant inconnu: " + request.fuelTypeName())))
        ).flatMap(tuple -> {
            // Une fois qu'on a toutes les entités, on construit l'objet Vehicle du domaine
            var make = tuple.getT1();
            var model = tuple.getT2();
            var trans = tuple.getT3();
            var manuf = tuple.getT4();
            var size = tuple.getT5();
            var type = tuple.getT6();
            var fuel = tuple.getT7();

            Vehicle vehicle = new Vehicle(
                null, // ID généré
                make.getVehicleMakeId(),
                model.getVehicleModelId(),
                trans.getTransmissionTypeId(),
                manuf.getManufacturerId(),
                size.getVehicleSizeId(),
                type.getVehicleTypeId(),
                fuel.getFuelTypeId(),
                request.vehicleSerialNumber(),
                request.vehicleSerialPhoto(),
                request.registrationNumber(),
                request.registrationPhoto(),
                null, // Expiry date
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

            // On délègue au UseCase standard
            return manageVehicleUseCase.createVehicle(vehicle);
        });
    }
}