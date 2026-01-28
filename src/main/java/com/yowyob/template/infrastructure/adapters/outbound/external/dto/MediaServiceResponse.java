package com.yowyob.template.infrastructure.adapters.outbound.external.dto;

import java.util.UUID;

public record MediaServiceResponse(
    UUID id,
    String uri,
    String name,
    String path,
    String mime,
    Long size
) {}