package de.paulkokot.imageservice.imageserviceserver.event;

import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ImageCreatedEvent extends ImageEvent {
    private final String resourceUrl;
    public ImageCreatedEvent(Object source, ImageMeta image, String resourceUrl) {
        super(source, image);
        this.resourceUrl = resourceUrl;
    }
}