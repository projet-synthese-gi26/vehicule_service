package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.ports.in.ManageVehicleMediaUseCase;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.VehicleResponse;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleIllustrationImageResponse;
import com.yowyob.template.infrastructure.mappers.VehicleDetailsMapper;
import com.yowyob.template.infrastructure.mappers.VehicleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Media", description = "Gestion des documents et photos des véhicules")
@SecurityRequirement(name = "bearerAuth")
public class VehicleMediaController {

    private final ManageVehicleMediaUseCase mediaUseCase;
    private final VehicleMapper vehicleMapper;
    private final VehicleDetailsMapper detailsMapper;

    // --- SERIAL PHOTO ---

    @PutMapping(value = "/{id}/documents/serial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Mettre à jour la photo N° Série (VIN)")
    public Mono<VehicleResponse> updateSerialPhoto(
            @PathVariable UUID id,
            @RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(file -> mediaUseCase.uploadSerialPhoto(id, file))
                .map(vehicleMapper::toResponse);
    }

    @DeleteMapping("/{id}/documents/serial")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer la photo N° Série")
    public Mono<Void> deleteSerialPhoto(@PathVariable UUID id) {
        return mediaUseCase.deleteSerialPhoto(id);
    }

    // --- REGISTRATION PHOTO ---

    @PutMapping(value = "/{id}/documents/registration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Mettre à jour la photo Carte Grise")
    public Mono<VehicleResponse> updateRegistrationPhoto(
            @PathVariable UUID id,
            @RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(file -> mediaUseCase.uploadRegistrationPhoto(id, file))
                .map(vehicleMapper::toResponse);
    }

    @DeleteMapping("/{id}/documents/registration")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer la photo Carte Grise")
    public Mono<Void> deleteRegistrationPhoto(@PathVariable UUID id) {
        return mediaUseCase.deleteRegistrationPhoto(id);
    }

    // --- ILLUSTRATIONS ---

    @GetMapping("/{id}/images")
    @Operation(summary = "Lister les images d'illustration")
    public Flux<VehicleIllustrationImageResponse> getIllustrations(@PathVariable UUID id) {
        return mediaUseCase.getIllustrationImages(id)
                .map(detailsMapper::toResponse);
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter une image d'illustration")
    public Mono<VehicleIllustrationImageResponse> addIllustration(
            @PathVariable UUID id,
            @RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(file -> mediaUseCase.addIllustrationImage(id, file))
                .map(detailsMapper::toResponse);
    }

    @DeleteMapping("/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une image d'illustration")
    public Mono<Void> deleteIllustration(@PathVariable UUID imageId) {
        // Note: On pourrait passer le vehicleId aussi pour vérifier les droits
        return mediaUseCase.deleteIllustrationImage(null, imageId);
    }
}