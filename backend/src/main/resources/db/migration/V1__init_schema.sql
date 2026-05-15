CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE titles (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(16) NOT NULL,
    name VARCHAR(512) NOT NULL,
    description TEXT,
    poster_url VARCHAR(1024),
    trailer_url VARCHAR(1024),
    external_ref VARCHAR(128),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL REFERENCES titles (id) ON DELETE CASCADE,
    season_number INT NOT NULL,
    episode_number INT NOT NULL,
    name VARCHAR(512) NOT NULL,
    UNIQUE (title_id, season_number, episode_number)
);

CREATE TABLE watchlist (
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title_id BIGINT NOT NULL REFERENCES titles (id) ON DELETE CASCADE,
    added_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, title_id)
);

CREATE TABLE watch_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title_id BIGINT NOT NULL REFERENCES titles (id) ON DELETE CASCADE,
    episode_id BIGINT REFERENCES episodes (id) ON DELETE SET NULL,
    position_seconds INT NOT NULL DEFAULT 0,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, title_id)
);

CREATE INDEX idx_watch_progress_user ON watch_progress (user_id);
CREATE INDEX idx_titles_name ON titles (name);
