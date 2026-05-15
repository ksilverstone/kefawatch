ALTER TABLE users ADD COLUMN email VARCHAR(128);
ALTER TABLE users ADD COLUMN first_name VARCHAR(64);
ALTER TABLE users ADD COLUMN last_name VARCHAR(64);

UPDATE users SET email = username || '@kefawatch.com', first_name = 'Kullanici', last_name = 'Kullanici' WHERE email IS NULL;

ALTER TABLE users ALTER COLUMN email SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT users_email_unique UNIQUE (email);
ALTER TABLE users ALTER COLUMN first_name SET NOT NULL;
ALTER TABLE users ALTER COLUMN last_name SET NOT NULL;
