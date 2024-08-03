package de.paulkokot.imageservice.imageserviceserver.model;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum ImageType {
    JPG("image/jpeg", "jpg", "jpg", "jpeg"),
    PNG("image/png", "png", "png"),
    GIF("image/gif", "gif", "gif");

    private final String mimeType;
    private final String fileSaveExtension;
    private final String[] fileExtensions;

    ImageType(String mimeType, String fileSaveExtension, String... fileExtensions) {
        this.mimeType = mimeType;
        this.fileSaveExtension = fileSaveExtension;
        if (fileExtensions.length == 0) {
            throw new IllegalArgumentException("At least one file extension must be provided");
        }
        this.fileExtensions = fileExtensions;
    }

    public static ImageType fromFileExtension(String fileExtension) {
        for (var imageType : values()) {
            for (var extension : imageType.fileExtensions) {
                if (extension.equals(fileExtension)) {
                    return imageType;
                }
            }
        }
        throw new IllegalArgumentException("Unknown file extension: " + fileExtension);
    }

    public static ImageType fromMimeType(String mimeType) {
        for (var imageType : values()) {
            if (imageType.mimeType.equals(mimeType)) {
                return imageType;
            }
        }
        throw new IllegalArgumentException("Unknown mime type: " + mimeType);
    }
}
