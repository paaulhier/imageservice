package de.paulkokot.imageservice.imageserviceserver.event;

import de.paulkokot.imageservice.imageserviceserver.model.Image;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true)
public class ImageDeletedEvent extends ApplicationEvent {
    private final Image image;

    public ImageDeletedEvent(Object source, Image image) {
        super(source);
        this.image = image;
    }
}
