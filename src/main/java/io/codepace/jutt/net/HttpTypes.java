package io.codepace.jutt.net;

/**
 * Enum for using various http types (GET, POST, etc.)
 */
public enum HttpTypes {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    PUT("PUT"),
    TRACE("TRACE"),
    OPTIONS("OPTIONS"),
    CONNECT("CONNECT"),
    DELETE("DELETE"),
    POST_PARAMS("POST_PARAMS"),
    PATCH("PATCH");

    private final String val;

    private HttpTypes(String s) {
        val = s;
    }

    public boolean equalsName(String otherName) {
        return val.equals(otherName);
    }

    public String toString() {
        return this.val;
    }
}
