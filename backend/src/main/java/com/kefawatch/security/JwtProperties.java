package com.kefawatch.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kefawatch.jwt")
public class JwtProperties {

    /**
     * HMAC secret; must be long enough for HS256 (>= 256 bits recommended).
     */
    private String secret = "change-this-dev-secret-min-32-chars-long!!";

    private long expirationMs = 86_400_000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
