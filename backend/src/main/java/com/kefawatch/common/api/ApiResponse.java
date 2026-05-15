package com.kefawatch.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic API envelope for consistent JSON responses.
 *
 * @param <T> payload type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, T data, String errorCode, String message) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> fail(String errorCode, String message) {
        return new ApiResponse<>(false, null, errorCode, message);
    }
}
