package com.yowyob.template.domain.model.vehicle;

import java.util.UUID;

public record Manufacturer(
        UUID manufacturerId,
        String manufacturerName) {
}
