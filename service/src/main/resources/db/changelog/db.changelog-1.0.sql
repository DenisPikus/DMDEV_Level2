--liquibase formatted sql

--changeset dpikus:1
CREATE SCHEMA store;

SET SCHEMA 'store';

--changeset dpikus:2
CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    firstname    VARCHAR(128)        NOT NULL,
    lastname     VARCHAR(128)        NOT NULL,
    email        VARCHAR(128) UNIQUE NOT NULL,
    password     VARCHAR(128)        NOT NULL,
    phone_number VARCHAR(128) UNIQUE NOT NULL,
    address      VARCHAR(128)        NOT NULL,
    role         VARCHAR(32)         NOT NULL
);

--changeset dpikus:3
CREATE TABLE product
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    brand        VARCHAR(128) NOT NULL,
    product_type         VARCHAR(128) NOT NULL,
    description  VARCHAR(255),
    price        DECIMAL(6, 2),
    availability BOOLEAN      NOT NULL,
    photo_path   VARCHAR(128),
    stock_id     BIGINT
);

--changeset dpikus:4
CREATE TABLE stock
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity   INTEGER,
    address    VARCHAR(128),
    FOREIGN KEY (product_id) REFERENCES Product (id) ON DELETE CASCADE
);

--changeset dpikus:5
CREATE TABLE orders
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    closing_date  TIMESTAMP,
    order_status   VARCHAR(32)
);

--changeset dpikus:6
CREATE TABLE orders_product
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