package com.yowyob.template.infrastructure.adapters.outbound.external;

import com.yowyob.template.domain.ports.out.MediaStoragePort;
import com.yowyob.template.infrastructure.adapters.outbound.external.dto.MediaServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpMediaAdapter implements MediaStoragePort {

    private final WebClient.Builder webClientBuilder;

    @Value("${application.external.media-service-url}")
    private String mediaServiceUrl;

    @Override
    public Mono<MediaServiceResponse> upload(FilePart file, String location) {
        WebClient client = webClientBuilder.baseUrl(mediaServiceUrl).build();

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);
        builder.part("service", "fleet-service");
        builder.part("location", location);

        return client.post()
                .uri("/media/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(MediaServiceResponse.class)
                .doOnSuccess(r -> log.info("Upload réussi: {}", r.uri()))
                .doOnError(e -> log.error("Erreur upload MediaService", e));
    }

    @Override
    public Mono<Void> delete(String mediaId) {
        if (mediaId == null || mediaId.isBlank()) return Mono.empty();
        
        WebClient client = webClientBuilder.baseUrl(mediaServiceUrl).build();
        return client.delete()
                .uri("/media/{id}", mediaId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> log.warn("Erreur suppression MediaService (ID: {}): {}", mediaId, e.getMessage()))
                .onErrorResume(e -> Mono.empty()); // On ne bloque pas si le fichier n'existe plus
    }
}