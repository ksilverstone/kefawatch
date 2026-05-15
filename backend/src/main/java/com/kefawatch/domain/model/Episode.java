package com.kefawatch.domain.model;

public record Episode(long id, long titleId, int seasonNumber, int episodeNumber, String name) {
}
