package com.yowyob.template.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.yowyob.template.domain.model.vehicle.details.VehicleAmenity;
import com.yowyob.template.domain.model.vehicle.details.VehicleCanTransport;
import com.yowyob.template.domain.model.vehicle.details.VehicleIllustrationImage;
import com.yowyob.template.domain.model.vehicle.details.VehicleKeyword;
import com.yowyob.template.domain.model.vehicle.details.VehicleReview;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleAmenityRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleAmenityResponse;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleCanTransportRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleCanTransportResponse;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleIllustrationImageRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleIllustrationImageResponse;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleKeywordRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleKeywordResponse;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleReviewRequest;
import com.yowyob.template.infrastructure.adapters.inbound.rest.dto.details.VehicleReviewResponse;

import com.yowyob.template.infrastructure.adapters.outbound.persistence.entity.*;

@Mapper(componentModel = "spring")
public interface VehicleDetailsMapper {

    VehicleAmenity toDomain(VehicleAmenityRequest request);

    VehicleAmenityResponse toResponse(VehicleAmenity domain);

    VehicleAmenityEntity toEntity(VehicleAmenity domain);

    VehicleAmenity toDomain(VehicleAmenityEntity entity);

    VehicleKeyword toDomain(VehicleKeywordRequest request);

    VehicleKeywordResponse toResponse(VehicleKeyword domain);

    VehicleKeywordEntity toEntity(VehicleKeyword domain);

    VehicleKeyword toDomain(VehicleKeywordEntity entity);

    VehicleCanTransport toDomain(VehicleCanTransportRequest request);

    VehicleCanTransportResponse toResponse(VehicleCanTransport domain);

    VehicleCanTransportEntity toEntity(VehicleCanTransport domain);

    VehicleCanTransport toDomain(VehicleCanTransportEntity entity);

    VehicleIllustrationImage toDomain(VehicleIllustrationImageRequest request);

    VehicleIllustrationImageResponse toResponse(VehicleIllustrationImage domain);

    VehicleIllustrationImageEntity toEntity(VehicleIllustrationImage domain);

    VehicleIllustrationImage toDomain(VehicleIllustrationImageEntity entity);

    VehicleReview toDomain(VehicleReviewRequest request);

    VehicleReviewResponse toResponse(VehicleReview domain);

    VehicleReviewEntity toEntity(VehicleReview domain);

    VehicleReview toDomain(VehicleReviewEntity entity);
}
