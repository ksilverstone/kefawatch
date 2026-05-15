package com.kefawatch.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProgressUpsertRequest(
        @NotNull Long titleId,
        Long episodeId,
        @Min(0) int positionSeconds,
        boolean completed
) {
}
