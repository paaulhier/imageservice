package de.paulkokot.imageservice.imageserviceserver.controller;

import de.paulkokot.imageservice.imageserviceserver.exception.ImageNotExistingException;
import de.paulkokot.imageservice.imageserviceserver.exception.ImageNotPresentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ImageNotExistingException.class)
    public ResponseEntity<String> handleImageNotExistingException(
            ImageNotExistingException e
    ) {
        return ResponseEntity.status(204).body("Sorry, but the image does not exist");
    }

    @ExceptionHandler(ImageNotPresentException.class)
    public ResponseEntity<String> handleImageNotPresentException(
            ImageNotPresentException e
    ) {
        return ResponseEntity.status(500).body("Sorry, but the image is not present.");
    }
}
