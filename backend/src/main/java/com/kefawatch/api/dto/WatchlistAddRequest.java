package com.kefawatch.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WatchlistAddRequest(@NotNull @Positive Long titleId) {
}
