package de.paulkokot.imageservice.imageserviceserver.event;

import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ImageServedEvent extends ImageEvent{
    private final long processingTime;
    public ImageServedEvent(Object source, ImageMeta image, long processingTime) {
        super(source, image);
        this.processingTime = processingTime;
    }
}
