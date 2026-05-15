package com.kefawatch.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTitleRequest(
        @NotBlank @Size(max = 16) String type,
        @NotBlank @Size(max = 512) String name,
        String description,
        String posterUrl,
        String trailerUrl,
        String externalRef
) {
}
