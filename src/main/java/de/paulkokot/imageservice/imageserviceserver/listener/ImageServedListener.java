package de.paulkokot.imageservice.imageserviceserver.listener;

import de.paulkokot.imageservice.imageserviceserver.event.ImageServedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ImageServedListener implements ApplicationListener<ImageServedEvent> {
    private static final Logger logger = Logger.getLogger(ImageServedListener.class.getName());

    @Override
    public void onApplicationEvent(ImageServedEvent event) {
        logger.info("Image with id %s was served. Took %sms.".formatted(
                event.imageMeta().image().imageId(),
                event.processingTime()
        ));
    }
}