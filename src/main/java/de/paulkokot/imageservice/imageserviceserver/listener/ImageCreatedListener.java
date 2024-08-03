package de.paulkokot.imageservice.imageserviceserver.listener;

import de.paulkokot.imageservice.imageserviceserver.event.ImageCreatedEvent;
import de.paulkokot.imageservice.imageserviceserver.model.Image;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ImageCreatedListener implements ApplicationListener<ImageCreatedEvent> {
    private static final Logger logger = Logger.getLogger(ImageCreatedListener.class.getName());

    @Override
    public void onApplicationEvent(ImageCreatedEvent event) {
        Image image = event.imageMeta().image();
        logger.info("Image with id %s was cached and saved. The image is accessible at the url %s.".formatted(
                image.imageId(),
                event.resourceUrl()
        ));
    }
}