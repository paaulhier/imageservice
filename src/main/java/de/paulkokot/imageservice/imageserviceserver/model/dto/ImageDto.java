package de.paulkokot.imageservice.imageserviceserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.paulkokot.imageservice.imageserviceserver.model.Image;
import de.paulkokot.imageservice.imageserviceserver.model.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link de.paulkokot.imageservice.imageserviceserver.model.Image}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto implements Serializable {
    private UUID imageId;
    private ImageType imageType;
    private LocalDateTime uploadedAt;
    private String uploaderIp;
    private boolean disableDeletion;
    private LocalDateTime deletedAt;
    private int height;
    private int width;

    public ImageDto(Image image) {
        this.imageId = image.imageId();
        this.imageType = image.imageType();
        this.uploadedAt = image.uploadedAt();
        this.uploaderIp = image.uploaderIp();
        this.disableDeletion = image.disableDeletion();
        this.deletedAt = image.deletedAt();
        this.height = image.height();
        this.width = image.width();
    }
}