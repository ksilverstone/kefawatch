package com.kefawatch.domain.port;

import java.util.List;

/**
 * NoSQL-backed popularity tracking (Redis sorted set).
 */
public interface TrendingTitleStore {

    void recordView(long titleId);

    List<Long> topIds(int limit);
}
