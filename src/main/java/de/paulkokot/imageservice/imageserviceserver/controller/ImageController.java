package de.paulkokot.imageservice.imageserviceserver.controller;

import de.paulkokot.imageservice.imageserviceserver.controller.image.ImageMeta;
import de.paulkokot.imageservice.imageserviceserver.event.ImageServedEvent;
import de.paulkokot.imageservice.imageserviceserver.image.ImageService;
import de.paulkokot.imageservice.imageserviceserver.model.dto.ImageMetaDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private static final Pattern uuidPattern = Pattern.compile(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
    );

    private final ApplicationEventPublisher eventPublisher;
    private final ImageService imageService;

    @GetMapping(value = "/", produces = MediaType.IMAGE_GIF_VALUE)
    public @ResponseBody ResponseEntity<byte[]> showIndex() {
        ImageMeta imageMeta = imageService.imageMeta("default.gif");
        return createServableResponseEntity(imageMeta);
    }

    @Async
    @GetMapping("/meta/{id}")
    public CompletableFuture<ResponseEntity<ImageMetaDto>> imageMeta(
            @PathVariable String id
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (!uuidPattern.matcher(id).matches()) {
                return ResponseEntity.badRequest().build();
            }

            ImageMeta imageMeta = imageService.imageMeta(id);
            if (imageMeta.image().deleted()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(new ImageMetaDto(imageMeta));
        });
    }

    @Async
    @GetMapping("/{id}")
    @ResponseBody
    public CompletableFuture<ResponseEntity<byte[]>> serveImage(
            @PathVariable String id
    ) {
        long start = System.currentTimeMillis();
        if (!uuidPattern.matcher(id).matches()) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().build()
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            ImageMeta imageMeta = imageService.imageMeta(id);
            if (imageMeta.image().deleted())  {
                return ResponseEntity.noContent().build();
            }
            ResponseEntity<byte[]> responseEntity = createServableResponseEntity(imageMeta);
            eventPublisher.publishEvent(new ImageServedEvent(
                    this,
                    imageMeta,
                    System.currentTimeMillis() - start
            ));
            return responseEntity;
        });
    }

    @Async
    @PostMapping("/")
    public CompletableFuture<ResponseEntity<String>> uploadImage(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        var requestUrl = request.getRequestURL().toString();
        return CompletableFuture.supplyAsync(() -> {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            try {
                ImageMeta imageMeta = imageService.saveImage(
                        extension,
                        request.getRemoteAddr(),
                        file.getBytes()
                );
                return ResponseEntity.status(201).body(
                        requestUrl + imageMeta.image().imageId()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ResponseEntity<byte[]> createServableResponseEntity(ImageMeta imageMeta) {
        return ResponseEntity.ok()
                .contentType(MediaType.asMediaType(MimeType.valueOf(
                        imageMeta.image().imageType().mimeType()
                )))
                .body(imageMeta.data());
    }
}