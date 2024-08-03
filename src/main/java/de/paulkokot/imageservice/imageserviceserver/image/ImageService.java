package de.paulkokot.imageservice.imageserviceserver.image;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;
import de.paulkokot.imageservice.imageserviceserver.event.ImageCreatedEvent;
import de.paulkokot.imageservice.imageserviceserver.event.ImageDeletedEvent;
import de.paulkokot.imageservice.imageserviceserver.exception.ImageNotExistingException;
import de.paulkokot.imageservice.imageserviceserver.exception.ImageNotPresentException;
import de.paulkokot.imageservice.imageserviceserver.model.Image;
import de.paulkokot.imageservice.imageserviceserver.model.ImageType;
import de.paulkokot.imageservice.imageserviceserver.repository.ImageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Service
public class ImageService {
    private static final Logger logger = Logger.getLogger(ImageService.class.getName());
    private static final File imageFolder = new File("images");

    private final LoadingCache<String, ImageMeta> imageCache;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageRepository imageRepository;

    @Value("${backend.base-url}")
    private String backendBaseUrl;

    public ImageService(
            ImageRepository imageRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.imageRepository = imageRepository;
        this.eventPublisher = eventPublisher;

        imageCache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(15))
                .removalListener(notification -> logger.info(
                        "Image with id " + notification.getKey() + " was removed from cache."
                ))
                .build(CacheLoader.asyncReloading(
                        CacheLoader.from(key -> {
                            try {
                                if (key.equals("default.gif")) {
                                    return defaultItemMeta();
                                }
                                UUID imageId = UUID.fromString(key);

                                Image image = imageRepository.findImageByImageIdAndDeletedAtIsNull(imageId).orElseThrow(
                                        () -> new ImageNotExistingException("Image does not exist")
                                );

                                return new ImageMeta(
                                        image,
                                        readImage(image),
                                        backendBaseUrl + image.imageId()
                                );
                            } catch (Throwable throwable) {
                                return defaultItemMeta();
                            }
                        }),
                        Executors.newFixedThreadPool(2)
                ));
    }

    private static ImageMeta defaultItemMeta() {
        try {
            System.out.println("Using default image");
            return defaultGifImageMeta(
                    Files.readAllBytes(new File("images/default.gif").toPath())
            );
        } catch (IOException e) {
            return defaultGifImageMeta(new byte[0]);
        }
    }

    @PostConstruct
    public void init() {
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
    }


    public void deleteImage(Image image) {
        if (image.disableDeletion()) return;
        imageRepository.save(image.delete());
        try {
            Files.deleteIfExists(new File(
                    imageFolder,
                    image.imagePath()
            ).toPath());
            invalidateCache(image);
            eventPublisher.publishEvent(new ImageDeletedEvent(
                    this,
                    image
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void invalidateCache(Image image) {
        imageCache.invalidate(image.imageId().toString());
        imageCache.cleanUp();
    }

    public ImageMeta saveImage(
            String extension,
            String uploaderIp,
            byte[] fileContent
    ) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(fileContent));
            Image image = writeImageToDiskAndSaveToRepository(
                    ImageType.fromFileExtension(extension),
                    uploaderIp,
                    fileContent,
                    bufferedImage.getHeight(),
                    bufferedImage.getWidth()
            );
            String resourceUrl = backendBaseUrl + image.imageId();

            ImageMeta imageMeta = new ImageMeta(
                    image,
                    fileContent,
                    bufferedImage.getHeight(),
                    bufferedImage.getWidth(),
                    resourceUrl
            );
            eventPublisher.publishEvent(new ImageCreatedEvent(
                    this,
                    imageMeta,
                    resourceUrl
            ));
            imageCache.put(
                    image.imageId().toString(),
                    imageMeta
            );
            return imageMeta;
        } catch (Throwable throwable) {
            return null;
        }
    }

    private Image writeImageToDiskAndSaveToRepository(
            ImageType imageType,
            String uploaderIp,
            byte[] imageData,
            int height,
            int width
    ) {
        UUID uuid = writeImageToDisk(
                imageData,
                imageType
        );
        Image image = new Image(
                uuid,
                uploaderIp,
                imageType,
                height,
                width
        );
        return imageRepository.save(image);
    }

    private UUID writeImageToDisk(
            byte[] imageData,
            ImageType imageType
    ) {
        UUID id = UUID.randomUUID();
        File imageFile = new File(imageFolder, "%s.%s".formatted(
                id.toString(),
                imageType.name().toLowerCase()
        ));
        if (imageFile.exists()) {
            return writeImageToDisk(
                    imageData,
                    imageType
            );
        }

        try {
            Files.write(imageFile.toPath(), imageData);
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Could not save image to disk.");
        }
    }

    public ImageMeta imageMeta(String imageId) {
        return imageCache.getUnchecked(imageId);
    }

    private Image image(UUID id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ImageNotExistingException("Image does not exist")
        );
    }

    public byte[] readImage(Image image) throws IOException {
        var imageFile = new File(imageFolder, image.imagePath());

        if (!imageFile.exists()) {
            throw new ImageNotPresentException("Image is not present.");
        }
        return Files.readAllBytes(imageFile.toPath());
    }

    private static ImageMeta defaultGifImageMeta(byte[] data) {
        return new ImageMeta(
                new Image(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "0.0.0.0",
                        ImageType.GIF,
                        720,
                        1280
                ),
                data
        );
    }

    private static ImageMeta defaultImageMeta() {
        return defaultGifImageMeta(new byte[0]);
    }
}