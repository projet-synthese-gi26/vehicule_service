package com.yowyob.template.infrastructure.adapters.inbound.rest;

import org.apache.kafka.clients.admin.CreateAclsOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yowyob.template.domain.model.vehicle.Vehicle;
import com.yowyob.template.domain.ports.in.CreateVehicleUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleResponse;
import com.yowyob.template.infrastructure.mappers.VehicleMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    public final CreateVehicleUseCase createVehicleUseCase;
    public final VehicleMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public Mono<VehicleResponse> createVehicle(@RequestBody VehicleRequest vehicleRequest) {
        return Mono.just(mapper.toDomain(vehicleRequest))
                .flatMap(createVehicleUseCase::createVehicle)
                .map(mapper::toResponse);
    }

}
