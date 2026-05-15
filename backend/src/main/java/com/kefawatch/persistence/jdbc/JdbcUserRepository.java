package com.kefawatch.persistence.jdbc;

import com.kefawatch.domain.model.UserAccount;
import com.kefawatch.domain.port.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<UserAccount> MAPPER = (rs, rowNum) -> new UserAccount(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password_hash")
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        var list = jdbcTemplate.query("SELECT id, username, password_hash FROM users WHERE username = ?", MAPPER, username);
        return list.stream().findFirst();
    }

    @Override
    public Optional<UserAccount> findById(long id) {
        var list = jdbcTemplate.query("SELECT id, username, password_hash FROM users WHERE id = ?", MAPPER, id);
        return list.stream().findFirst();
    }

    @Override
    public long insert(String username, String passwordHash) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users (username, password_hash) VALUES (?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
