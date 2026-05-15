package com.kefawatch.app.network.dto;

public class TokenEnvelope {
    public boolean success;
    public TokenData data;
    public String errorCode;
    public String message;

    public static class TokenData {
        public String accessToken;
    }
}
