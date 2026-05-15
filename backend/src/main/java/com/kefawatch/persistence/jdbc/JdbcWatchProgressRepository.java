package com.kefawatch.persistence.jdbc;

import com.kefawatch.domain.model.WatchProgress;
import com.kefawatch.domain.port.WatchProgressRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcWatchProgressRepository implements WatchProgressRepository {

    private static final RowMapper<WatchProgress> MAPPER = (rs, rowNum) -> new WatchProgress(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getLong("title_id"),
            rs.getObject("episode_id", Long.class),
            rs.getInt("position_seconds"),
            rs.getBoolean("completed"),
            rs.getObject("updated_at", Timestamp.class).toInstant()
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcWatchProgressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public WatchProgress upsert(long userId, long titleId, Long episodeId, int positionSeconds, boolean completed) {
        Optional<WatchProgress> existing = findByUserAndTitle(userId, titleId);
        if (existing.isEmpty()) {
            jdbcTemplate.update(
                    """
                            INSERT INTO watch_progress (user_id, title_id, episode_id, position_seconds, completed)
                            VALUES (?, ?, ?, ?, ?)
                            """,
                    userId,
                    titleId,
                    episodeId,
                    positionSeconds,
                    completed
            );
        } else {
            jdbcTemplate.update(
                    """
                            UPDATE watch_progress
                            SET episode_id = ?, position_seconds = ?, completed = ?, updated_at = NOW()
                            WHERE user_id = ? AND title_id = ?
                            """,
                    episodeId,
                    positionSeconds,
                    completed,
                    userId,
                    titleId
            );
        }
        return findByUserAndTitle(userId, titleId).orElseThrow();
    }

    @Override
    public Optional<WatchProgress> findByUserAndTitle(long userId, long titleId) {
        List<WatchProgress> list = jdbcTemplate.query(
                "SELECT id, user_id, title_id, episode_id, position_seconds, completed, updated_at FROM watch_progress WHERE user_id = ? AND title_id = ?",
                MAPPER,
                userId,
                titleId
        );
        return list.stream().findFirst();
    }

    @Override
    public List<WatchProgress> findByUserId(long userId) {
        return jdbcTemplate.query(
                "SELECT id, user_id, title_id, episode_id, position_seconds, completed, updated_at FROM watch_progress WHERE user_id = ?",
                MAPPER,
                userId
        );
    }
}
