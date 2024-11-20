CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS post(
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    user_id BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS post_comment(
    id BIGSERIAL PRIMARY KEY,
    comment TEXT,
    post_id BIGINT REFERENCES post(id)
);

