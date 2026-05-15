package com.kefawatch.domain.model;

import java.time.Instant;

public record WatchlistEntry(long titleId, String titleName, String type, Instant addedAt) {
}
