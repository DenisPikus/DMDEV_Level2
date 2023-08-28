--liquibase formatted sql

--changeset dpikus:1
ALTER TABLE users
ADD COLUMN image VARCHAR(64);
--rollback DROP TABLE users;
