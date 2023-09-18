--liquibase formatted sql

--changeset dpikus:1
ALTER TABLE users
    ALTER COLUMN role SET DEFAULT 'USER';
--rollback ALTER TABLE users ALTER COLUMN role DROP DEFAULT;