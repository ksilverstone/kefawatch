package com.kefawatch.persistence.redis;

import com.kefawatch.domain.port.TrendingTitleStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class RedisTrendingTitleStore implements TrendingTitleStore {

    static final String TRENDING_KEY = "trending:titles";

    private final StringRedisTemplate redisTemplate;

    public RedisTrendingTitleStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void recordView(long titleId) {
        redisTemplate.opsForZSet().incrementScore(TRENDING_KEY, String.valueOf(titleId), 1.0);
    }

    @Override
    public List<Long> topIds(int limit) {
        Set<String> range = redisTemplate.opsForZSet().reverseRange(TRENDING_KEY, 0, Math.max(0, limit - 1));
        if (range == null || range.isEmpty()) {
            return List.of();
        }
        List<Long> ids = new ArrayList<>();
        for (String s : range) {
            ids.add(Long.parseLong(Objects.requireNonNull(s)));
        }
        return ids;
    }
}
