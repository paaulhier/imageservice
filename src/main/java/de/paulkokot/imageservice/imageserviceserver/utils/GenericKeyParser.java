package de.paulkokot.imageservice.imageserviceserver.utils;

public interface GenericKeyParser<T> {

    T parse(byte[] keyBytes) throws Exception;

}