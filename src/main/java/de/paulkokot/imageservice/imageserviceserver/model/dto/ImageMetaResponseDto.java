package de.paulkokot.imageservice.imageserviceserver.model.dto;

import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;

public record ImageMetaResponseDto(
        String imageId,
        ImageMeta imageMeta
) {
}