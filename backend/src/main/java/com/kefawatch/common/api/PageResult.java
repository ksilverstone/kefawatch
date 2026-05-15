package com.kefawatch.common.api;

import java.util.List;

/**
 * Generic page wrapper for list endpoints.
 *
 * @param <T> element type
 */
public record PageResult<T>(List<T> content, int page, int size, long totalElements) {
}
