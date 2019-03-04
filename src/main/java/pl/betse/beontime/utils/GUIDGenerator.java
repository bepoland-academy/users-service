package pl.betse.beontime.utils;

import java.util.UUID;

public class GUIDGenerator {
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
