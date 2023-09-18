--liquibase formatted sql

--changeset dpikus:1
ALTER TABLE product
    ADD COLUMN IF NOT EXISTS quantity INTEGER;
--rollback ALTER TABLE product DROP COLUMN quantity;