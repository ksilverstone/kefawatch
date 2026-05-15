package com.kefawatch.domain.port;

import com.kefawatch.domain.model.WatchProgress;

import java.util.Optional;

public interface WatchProgressRepository {

    WatchProgress upsert(long userId, long titleId, Long episodeId, int positionSeconds, boolean completed);

    Optional<WatchProgress> findByUserAndTitle(long userId, long titleId);

    java.util.List<WatchProgress> findByUserId(long userId);
}
