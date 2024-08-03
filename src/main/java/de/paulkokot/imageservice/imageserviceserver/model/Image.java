package de.paulkokot.imageservice.imageserviceserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Accessors(fluent = true)
public class Image {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private UUID imageId;

    private ImageType imageType;

    private int width;
    private int height;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
    private String uploaderIp;

    private boolean disableDeletion;
    private LocalDateTime deletedAt;

    public Image(
            UUID uuid,
            String uploaderIp,
            ImageType imageType,
            int height,
            int width
    ) {
        this.imageId = uuid;
        this.uploaderIp = uploaderIp;
        this.imageType = imageType;
        this.height = height;
        this.width = width;
    }

    public Image() {

    }

    public String imagePath() {
        return imageId.toString() + "." + imageType.fileExtensions()[0];
    }

    public boolean deleted() {
        return deletedAt != null;
    }

    public Image delete() {
        this.deletedAt = LocalDateTime.now();
        return this;
    }
}