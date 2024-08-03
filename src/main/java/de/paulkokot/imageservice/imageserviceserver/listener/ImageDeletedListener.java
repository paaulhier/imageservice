package de.paulkokot.imageservice.imageserviceserver.listener;

import de.paulkokot.imageservice.imageserviceserver.event.ImageDeletedEvent;
import de.paulkokot.imageservice.imageserviceserver.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ImageDeletedListener implements ApplicationListener<ImageDeletedEvent> {
    private static final Logger logger = Logger.getLogger(ImageDeletedListener.class.getName());

    private final ImageService imageService;

    @Override
    public void onApplicationEvent(ImageDeletedEvent event) {
        logger.info("Image with id " + event.image().imageId() + " got deleted.");
    }
}