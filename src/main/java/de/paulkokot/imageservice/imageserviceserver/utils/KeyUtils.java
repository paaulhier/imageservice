package de.paulkokot.imageservice.imageserviceserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.function.Function;

public class KeyUtils {

    public static <T> T read(File file, GenericKeyParser<T> parser) {
        try (var fis = new FileInputStream(file)) {
            byte[] keyBytes = fis.readAllBytes();
            return parser.parse(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void write(File file, Function<T, byte[]> serializer, T t) {
        try (var fos = new FileOutputStream(file)) {
            fos.write(serializer.apply(t));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}