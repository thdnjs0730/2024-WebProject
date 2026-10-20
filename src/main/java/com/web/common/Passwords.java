package com.web.common;
import org.mindrot.jbcrypt.BCrypt;
public final class Passwords {
    private Passwords() {}
    public static boolean valid(String value) {
        return value != null && value.length() >= 8 && value.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72;
    }
    public static String hash(String value) {
        if (!valid(value)) throw new IllegalArgumentException("Password must be at least 8 characters and at most 72 UTF-8 bytes");
        return BCrypt.hashpw(value, BCrypt.gensalt(12));
    }
    public static boolean matches(String raw, String encoded) {
        if (raw == null || encoded == null || !encoded.startsWith("$2")) return false;
        try { return BCrypt.checkpw(raw, encoded); } catch (IllegalArgumentException e) { return false; }
    }
}
