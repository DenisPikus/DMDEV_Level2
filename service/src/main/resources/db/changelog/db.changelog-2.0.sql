--liquibase formatted sql

--changeset dpikus:1
ALTER TABLE users
ADD COLUMN IF NOT EXISTS image VARCHAR(64);
--rollback ALTER TABLE users DROP COLUMN image;
