package com.yowyob.template.application.service;

import org.springframework.stereotype.Service;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.CreateVehicleUseCase;
import com.yowyob.template.domain.ports.out.VehicleRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService implements CreateVehicleUseCase {

    public final VehicleRepositoryPort repositoryPort;

    @Override
    public Mono<Vehicle> createVehicle(Vehicle vehicle) {

        log.info("vehicule en cours de sauvegarde");

        return repositoryPort.saveVehicle(vehicle);

    }

}
