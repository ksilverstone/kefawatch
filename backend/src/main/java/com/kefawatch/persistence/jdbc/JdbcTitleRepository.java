package com.kefawatch.persistence.jdbc;

import com.kefawatch.common.api.PageResult;
import com.kefawatch.domain.model.Episode;
import com.kefawatch.domain.model.TitleDetail;
import com.kefawatch.domain.model.TitleSummary;
import com.kefawatch.domain.port.TitleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTitleRepository implements TitleRepository {

    private static final RowMapper<TitleSummary> SUMMARY_MAPPER = (rs, rowNum) -> new TitleSummary(
            rs.getLong("id"),
            rs.getString("type"),
            rs.getString("name"),
            rs.getString("poster_url")
    );

    private static final RowMapper<Episode> EPISODE_MAPPER = (rs, rowNum) -> new Episode(
            rs.getLong("id"),
            rs.getLong("title_id"),
            rs.getInt("season_number"),
            rs.getInt("episode_number"),
            rs.getString("name")
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcTitleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PageResult<TitleSummary> findPage(int page, int size) {
        Long totalObj = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM titles", Long.class);
        long total = totalObj == null ? 0L : totalObj;
        int offset = page * size;
        List<TitleSummary> rows = jdbcTemplate.query(
                "SELECT id, type, name, poster_url FROM titles ORDER BY id LIMIT ? OFFSET ?",
                SUMMARY_MAPPER,
                size,
                offset
        );
        return new PageResult<>(rows, page, size, total);
    }

    @Override
    public Optional<TitleDetail> findDetailById(long id) {
        List<TitleDetail> rows = jdbcTemplate.query(
                """
                        SELECT id, type, name, description, poster_url, trailer_url, external_ref
                        FROM titles WHERE id = ?
                        """,
                (rs, rowNum) -> new TitleDetail(
                        rs.getLong("id"),
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("poster_url"),
                        rs.getString("trailer_url"),
                        rs.getString("external_ref"),
                        List.of()
                ),
                id
        );
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        TitleDetail base = rows.get(0);
        List<Episode> episodes = findEpisodesByTitleId(id);
        return Optional.of(new TitleDetail(
                base.id(),
                base.type(),
                base.name(),
                base.description(),
                base.posterUrl(),
                base.trailerUrl(),
                base.externalRef(),
                episodes
        ));
    }

    @Override
    public List<Episode> findEpisodesByTitleId(long titleId) {
        return jdbcTemplate.query(
                "SELECT id, title_id, season_number, episode_number, name FROM episodes WHERE title_id = ? ORDER BY season_number, episode_number",
                EPISODE_MAPPER,
                titleId
        );
    }

    @Override
    public boolean existsById(long id) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM titles WHERE id = ?", Long.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<TitleSummary> findSummariesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        List<TitleSummary> rows = jdbcTemplate.query(
                "SELECT id, type, name, poster_url FROM titles WHERE id IN (" + inSql + ")",
                SUMMARY_MAPPER,
                ids.toArray()
        );
        Map<Long, TitleSummary> byId = new HashMap<>();
        for (TitleSummary row : rows) {
            byId.put(row.id(), row);
        }
        List<TitleSummary> ordered = new ArrayList<>();
        for (Long id : ids) {
            TitleSummary s = byId.get(id);
            if (s != null) {
                ordered.add(s);
            }
        }
        return ordered;
    }

    @Override
    public PageResult<TitleSummary> search(String query, int page, int size) {
        String likeQuery = "%" + query.toLowerCase() + "%";
        Long totalObj = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM titles WHERE LOWER(name) LIKE ?", Long.class, likeQuery);
        long total = totalObj == null ? 0L : totalObj;
        int offset = page * size;
        List<TitleSummary> rows = jdbcTemplate.query(
                "SELECT id, type, name, poster_url FROM titles WHERE LOWER(name) LIKE ? ORDER BY id LIMIT ? OFFSET ?",
                SUMMARY_MAPPER,
                likeQuery, size, offset
        );
        return new PageResult<>(rows, page, size, total);
    }

    @Override
    public long insert(String type, String name, String description, String posterUrl, String trailerUrl, String externalRef) {
        org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO titles (type, name, description, poster_url, trailer_url, external_ref) VALUES (?, ?, ?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, type);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setString(4, posterUrl);
            ps.setString(5, trailerUrl);
            ps.setString(6, externalRef);
            return ps;
        }, keyHolder);
        return java.util.Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
