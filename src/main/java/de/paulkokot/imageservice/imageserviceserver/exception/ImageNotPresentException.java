package de.paulkokot.imageservice.imageserviceserver.exception;

public class ImageNotPresentException extends RuntimeException {
    public ImageNotPresentException(String message) {
        super(message);
    }
}
