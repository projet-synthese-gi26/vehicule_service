package com.yowyob.template.domain.ports.out;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface MediaStoragePort {
    /**
     * Upload un fichier vers le stockage externe.
     * @return L'URL publique du fichier + L'ID du média (encapsulés ou juste l'URL selon besoin)
     * Ici on renvoie l'objet complet réponse pour avoir l'ID et l'URI.
     */
    Mono<com.yowyob.template.infrastructure.adapters.outbound.external.dto.MediaServiceResponse> upload(FilePart file, String location);

    /**
     * Supprime un fichier du stockage externe via son ID.
     */
    Mono<Void> delete(String mediaId);
}