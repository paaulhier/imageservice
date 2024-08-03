package de.paulkokot.imageservice.imageserviceserver.model.dto;

import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;

public record ImageMetaDto(
        ImageDto image,
        String resourceUrl
) {

    public ImageMetaDto(ImageMeta imageMeta) {
        this(
                new ImageDto(imageMeta.image()),
                imageMeta.resourceUrl()
        );
    }
}