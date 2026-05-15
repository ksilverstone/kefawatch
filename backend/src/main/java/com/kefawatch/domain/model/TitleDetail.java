package com.kefawatch.domain.model;

import java.util.List;

public record TitleDetail(long id, String type, String name, String description, String posterUrl,
                          String trailerUrl, String externalRef, List<Episode> episodes) {
}
