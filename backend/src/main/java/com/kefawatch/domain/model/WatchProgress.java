package com.kefawatch.domain.model;

import java.time.Instant;

public record WatchProgress(long id, long userId, long titleId, Long episodeId, int positionSeconds,
                            boolean completed, Instant updatedAt) {
}
