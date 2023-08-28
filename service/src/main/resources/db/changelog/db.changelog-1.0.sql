--liquibase formatted sql

--changeset dpikus:1
CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    firstname    VARCHAR(128)        NOT NULL,
    lastname     VARCHAR(128)        NOT NULL,
    email        VARCHAR(128) UNIQUE NOT NULL,
    password     VARCHAR(128),
    phone_number VARCHAR(128) UNIQUE NOT NULL,
    address      VARCHAR(128)        NOT NULL,
    role         VARCHAR(32)         NOT NULL
);
--rollback DROP TABLE users;

--changeset dpikus:2
CREATE TABLE IF NOT EXISTS product
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    brand        VARCHAR(128) NOT NULL,
    product_type VARCHAR(128) NOT NULL,
    description  VARCHAR(255),
    price        DECIMAL(6, 2),
    availability BOOLEAN      NOT NULL,
    photo_path   VARCHAR(128),
    stock_id     BIGINT
);
--rollback DROP TABLE product;

--changeset dpikus:3
CREATE TABLE IF NOT EXISTS stock
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity   INTEGER,
    address    VARCHAR(128),
    FOREIGN KEY (product_id) REFERENCES Product (id) ON DELETE CASCADE
);
--rollback DROP TABLE stock;

--changeset dpikus:4
CREATE TABLE IF NOT EXISTS orders
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    closing_date  TIMESTAMP,
    order_status  VARCHAR(32)
);
--rollback DROP TABLE orders;

--changeset dpikus:5
CREATE TABLE IF NOT EXISTS order_item
(
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    product_id BIGINT not null,
    quantity   INTEGER,
    price      DECIMAL(6, 2),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    UNIQUE (order_id, product_id)
);
--rollback DROP TABLE order_item;