--liquibase formatted sql

--changeset dpikus:1
ALTER TABLE product
    ALTER COLUMN availability SET DEFAULT FALSE;
--rollback ALTER TABLE product ALTER COLUMN availability DROP DEFAULT;