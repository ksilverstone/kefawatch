package com.kefawatch.domain.port;

import com.kefawatch.domain.model.WatchlistEntry;

import java.util.List;

public interface WatchlistRepository {

    void add(long userId, long titleId);

    void remove(long userId, long titleId);

    List<WatchlistEntry> listByUser(long userId);

    boolean exists(long userId, long titleId);
}
