package de.paulkokot.imageservice.imageserviceserver.controller.image;

import de.paulkokot.imageservice.imageserviceserver.model.Image;

public record ImageMeta(
        Image image,
        byte[] data,
        int height,
        int width,
        String resourceUrl
) {

    public ImageMeta(Image image, byte[] data, String resourceUrl) {
        this(image, data, 0, 0, resourceUrl);
    }

    public ImageMeta(Image image, byte[] data) {
        this(image, data, 0, 0, null);
    }
}