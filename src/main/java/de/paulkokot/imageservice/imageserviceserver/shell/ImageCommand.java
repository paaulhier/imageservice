package de.paulkokot.imageservice.imageserviceserver.shell;

import de.paulkokot.imageservice.imageserviceserver.image.ImageService;
import de.paulkokot.imageservice.imageserviceserver.model.Image;
import de.paulkokot.imageservice.imageserviceserver.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

@ShellComponent
@RequiredArgsConstructor
public class ImageCommand {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final Logger logger = Logger.getLogger(ImageCommand.class.getName());

    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @ShellMethod("Show all images")
    public void images(
            @ShellOption(value = "--page", defaultValue = "1") Integer page
    ) {
        Page<Image> pageItems = imageRepository.findAllByDeletedAtIsNull(PageRequest.of(
                page - 1,
                10
        ));
        if (pageItems.isEmpty()) {
            pageItems = imageRepository.findAll(PageRequest.of(0, 10));
        }
        int totalPages = pageItems.getTotalPages();
        long totalElements = pageItems.getTotalElements();
        int pageNumber = pageItems.getPageable().getPageNumber();

        logger.info("Page %d of %d (%s items in total)".formatted(
                (pageNumber + 1),
                totalPages,
                totalElements
        ));

        pageItems.forEach(image -> logger.info("Image: %s - %s".formatted(
                image.imageId().toString(),
                dateTimeFormatter.format(image.uploadedAt())
        )));
    }

    @ShellMethod("Show image by id")
    public void image(
            @ShellOption(value = "--id") String id
    ) {
        imageRepository.findById(UUID.fromString(id)).ifPresentOrElse(
                image -> logger.info("Image: %s - %s (Uploaded from: %s)".formatted(
                        image.imageId().toString(),
                        dateTimeFormatter.format(image.uploadedAt()),
                        image.uploaderIp()
                )),
                () -> logger.severe("Image with id %s does not exist".formatted(id))
        );
    }

    @ShellMethod("Delete image by id")
    public void deleteImage(
            @ShellOption(value = "--id") String id
    ) {
        imageRepository.findById(UUID.fromString(id)).ifPresentOrElse(
                imageService::deleteImage,
                () -> logger.severe("Image with id %s does not exist".formatted(id))
        );
    }

}