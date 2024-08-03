package de.paulkokot.imageservice.imageserviceserver.event;

import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true)
public class ImageEvent extends ApplicationEvent {
    private final ImageMeta imageMeta;

    public ImageEvent(Object source, ImageMeta imageMeta) {
        super(source);
        this.imageMeta = imageMeta;
    }
}