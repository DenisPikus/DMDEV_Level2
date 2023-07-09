CREATE SCHEMA store;

SET SCHEMA 'store';

-- Create users table
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

-- Create product table
CREATE TABLE product
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    brand        VARCHAR(128) NOT NULL,
    type         VARCHAR(128) NOT NULL,
    description  VARCHAR(255),
    price        DECIMAL(6, 2),
    availability BOOLEAN      NOT NULL,
    photo_path   VARCHAR(128)
);

-- Create table stock
CREATE TABLE stock
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity   INTEGER,
    address    VARCHAR(128),
    FOREIGN KEY (product_id) REFERENCES Product (id) ON DELETE CASCADE
);

-- Create orders table
CREATE TABLE orders
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    closing_date  TIMESTAMP,
    status        VARCHAR(32)
);

-- Create orders_product table
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

-- Fill in users table
INSERT INTO users (firstname, lastname, email, password, phone_number, address, role)
VALUES ('Ivan', 'Ivanov', 'ivan@gmail.com', 'pass', '1234567890', 'BY, Minsk, 123 Sovetskaja St', 'USER'),
       ('Sergey', 'Sergeev', 'sergey@gmail.com', 'pass', '9876543210', 'BY, Minsk, 121 Sovetskaja St', 'USER'),
       ('Viktor', 'Pupkin', 'viktor@gmail.com', 'pass', '5555555555', 'BY, Minsk, 1 Sovetskaja St', 'ADMIN');

-- Fill in product table
INSERT INTO product (name, brand, type, description, price, availability, photo_path)
VALUES ('Spinnerbait', 'SHIMANO', 'BAIT', 'A versatile lure with spinning blades.', 9.99, true, null),
       ('Crankbait', 'MEGABASS', 'BAIT', 'A lure with a diving bill for deep fishing.', 7.99, true, null),
       ('Jig', 'ZIPBAITS', 'BAIT', 'A weighted lure with a soft plastic or feather tail.', 5.99, false, null);

-- Fill in stock table
INSERT INTO stock (product_id, quantity, address)
VALUES (1, 100, 'BY, Minsk'),
       (2, 100, 'BY, Minsk'),
       (3, 0, 'BY, Minsk');

-- Fill in orders table
INSERT INTO orders (user_id, creation_date, closing_date, status)
VALUES (1, '2023-07-01', null, 'PROCESSING'),
       (2, '2023-06-30', '2023-06-30', 'COMPLETED');

-- Fill in orders_product table
INSERT INTO orders_product (order_id, product_id, quantity, price)
VALUES (1, 1, 1, 9.99),
       (1, 2, 2, 7.99),
       (2, 3, 1, 5.99),
       (2, 1, 3, 9.99);
