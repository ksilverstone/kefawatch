package com.kefawatch.persistence.jdbc;

import com.kefawatch.domain.model.WatchlistEntry;
import com.kefawatch.domain.port.WatchlistRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcWatchlistRepository implements WatchlistRepository {

    private static final RowMapper<WatchlistEntry> MAPPER = (rs, rowNum) -> new WatchlistEntry(
            rs.getLong("title_id"),
            rs.getString("title_name"),
            rs.getString("type"),
            rs.getObject("added_at", Timestamp.class).toInstant()
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcWatchlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(long userId, long titleId) {
        jdbcTemplate.update(
                "INSERT INTO watchlist (user_id, title_id) VALUES (?, ?) ON CONFLICT (user_id, title_id) DO NOTHING",
                userId,
                titleId
        );
    }

    @Override
    public void remove(long userId, long titleId) {
        jdbcTemplate.update("DELETE FROM watchlist WHERE user_id = ? AND title_id = ?", userId, titleId);
    }

    @Override
    public List<WatchlistEntry> listByUser(long userId) {
        return jdbcTemplate.query(
                """
                        SELECT w.title_id, t.name AS title_name, t.type, w.added_at
                        FROM watchlist w
                        JOIN titles t ON t.id = w.title_id
                        WHERE w.user_id = ?
                        ORDER BY w.added_at DESC
                        """,
                MAPPER,
                userId
        );
    }

    @Override
    public boolean exists(long userId, long titleId) {
        Long c = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM watchlist WHERE user_id = ? AND title_id = ?",
                Long.class,
                userId,
                titleId
        );
        return c != null && c > 0;
    }
}
