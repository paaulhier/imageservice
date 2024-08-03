package de.paulkokot.imageservice.imageserviceserver.scheduler;

import de.paulkokot.imageservice.imageserviceserver.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class DeletionCheckTask {
    private final ImageRepository imageRepository;

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldImages() {
        var oldestImageTimestamp = Instant.now().minus(
                90,
                ChronoUnit.DAYS
        );

        imageRepository.findImagesByUploadedAtBefore(LocalDateTime.ofInstant(
                        oldestImageTimestamp,
                        ZoneId.systemDefault()
                ))
                .forEach(image -> {
                    try {
                        Files.deleteIfExists(Paths.get("images/%s.%s".formatted(
                                image.imageId().toString(),
                                image.imageType().fileSaveExtension()
                        )));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imageRepository.delete(image);
                });
    }
}