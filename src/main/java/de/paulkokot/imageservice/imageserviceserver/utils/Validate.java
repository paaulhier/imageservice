package de.paulkokot.imageservice.imageserviceserver.utils;

public final class Validate {

    public static void notNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

}