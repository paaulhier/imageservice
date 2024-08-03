package de.paulkokot.imageservice.imageserviceserver.utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Formatter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final long BYTE = 1L;
    private static final long KiB = BYTE << 10;
    private static final long MiB = KiB << 10;
    private static final long GiB = MiB << 10;
    private static final long TiB = GiB << 10;
    private static final long PiB = TiB << 10;
    private static final long EiB = PiB << 10;
    private static final long KB = BYTE * 1000;
    private static final long MB = KB * 1000;
    private static final long GB = MB * 1000;
    private static final long TB = GB * 1000;
    private static final long PB = TB * 1000;
    private static final long EB = PB * 1000;

    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#.##");

    private static String formatSize(long size, long divider, String unitName) {
        return DEC_FORMAT.format((double) size / divider) + " " + unitName;
    }

    public static String toHumanReadableBinaryPrefixes(long size) {
        if (size < 0)
            throw new IllegalArgumentException("Invalid file size: " + size);
        if (size >= EiB) return formatSize(size, EiB, "EiB");
        if (size >= PiB) return formatSize(size, PiB, "PiB");
        if (size >= TiB) return formatSize(size, TiB, "TiB");
        if (size >= GiB) return formatSize(size, GiB, "GiB");
        if (size >= MiB) return formatSize(size, MiB, "MiB");
        if (size >= KiB) return formatSize(size, KiB, "KiB");
        return formatSize(size, BYTE, "Bytes");
    }

    public static String toHumanReadableSIPrefixes(long size) {
        if (size < 0)
            throw new IllegalArgumentException("Invalid file size: " + size);
        if (size >= EB) return formatSize(size, EB, "EB");
        if (size >= PB) return formatSize(size, PB, "PB");
        if (size >= TB) return formatSize(size, TB, "TB");
        if (size >= GB) return formatSize(size, GB, "GB");
        if (size >= MB) return formatSize(size, MB, "MB");
        if (size >= KB) return formatSize(size, KB, "KB");
        return formatSize(size, BYTE, "Bytes");
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    public static String formatDate(LocalDate date) {
        return dateFormatter.format(date);
    }

}