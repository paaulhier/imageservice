package de.paulkokot.imageservice.imageserviceserver.exception;

public class ImageNotExistingException extends RuntimeException{
    public ImageNotExistingException(String message) {
        super(message);
    }
}