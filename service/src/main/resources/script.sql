CREATE SCHEMA store;

SET SCHEMA 'store';

CREATE TABLE Users
(
    id           BIGSERIAL PRIMARY KEY,
    firstname    VARCHAR(128) NOT NULL,
    lastname     VARCHAR(128) NOT NULL,
    email        VARCHAR(128) UNIQUE NOT NULL,
    password     VARCHAR(128) NOT NULL,
    phone_number VARCHAR(128) UNIQUE NOT NULL,
    address      VARCHAR(128) NOT NULL,
    role         VARCHAR(32)  NOT NULL
);

-- Create Products table
CREATE TABLE Products
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    brand_id     BIGINT       NOT NULL,
    description  VARCHAR(255),
    price        DECIMAL(6, 2),
    availability BOOLEAN      NOT NULL,
    photo_path VARCHAR(128)
);

-- Create Brand table
CREATE TABLE Brand
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) UNIQUE NOT NULL
);

-- Create table Stock
CREATE TABLE Stock
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity   INTEGER,
    FOREIGN KEY (product_id) REFERENCES Products (id) ON DELETE CASCADE
);

-- Create Cart table
CREATE TABLE Cart
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    product_id BIGINT,
    quantity   INTEGER,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products (id) ON DELETE CASCADE
);

-- Create Orders table
CREATE TABLE Orders
(
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    order_creation_date DATE,
    order_closing_date  DATE,
    status              VARCHAR(32)
);

-- Create OrderItems table
CREATE TABLE OrderItems
(
    id       BIGSERIAL PRIMARY KEY,
    order_id BIGINT,
    cart_id  BIGINT,
    FOREIGN KEY (order_id) REFERENCES Orders (id) ON DELETE CASCADE,
    FOREIGN KEY (cart_id) REFERENCES Cart (id) ON DELETE CASCADE
);

-- Fill in Users table
INSERT INTO Users (firstname, lastname, email, password, phone_number, address, role)
VALUES ('Ivan', 'Ivanov', 'ivan@gmail.com', 'pass', '1234567890', 'BY, Minsk, 123 Sovetskaja St', 'USER'),
       ('Sergey', 'Sergeev', 'sergey@gmail.com', 'pass', '9876543210', 'BY, Minsk, 121 Sovetskaja St', 'USER'),
       ('Viktor', 'Pupkin', 'viktor@gmail.com', 'pass', '5555555555', 'BY, Minsk, 1 Sovetskaja St', 'ADMIN');

-- Fill in Products table
INSERT INTO Products (name, brand_id, description, price, availability, photo_path)
VALUES ('Spinnerbait', 1, 'A versatile lure with spinning blades.', 9.99, true, null),
       ('Crankbait', 2, 'A lure with a diving bill for deep fishing.', 7.99, true, null),
       ('Jig', 3, 'A weighted lure with a soft plastic or feather tail.', 5.99, false, null);

-- Fill in Brand table
INSERT INTO Brand (name)
VALUES ('Shimano'),
       ('Megabass'),
       ('ZipBaits');

-- Fill in Cart table
INSERT INTO Stock (product_id, quantity)
VALUES (1, 100),
       (2, 100),
       (3, 0);

-- Fill in Stock table
INSERT INTO Cart (user_id, product_id, quantity)
VALUES (1, 1, 2),
       (1, 2, 1),
       (2, 1, 1),
       (2, 2, 1);

-- Fill in Orders table
INSERT INTO Orders (user_id, order_creation_date, order_closing_date, status)
VALUES (1, '2023-07-01', null, 'PROCESSING'),
       (2, '2023-06-30', '2023-06-30', 'COMPLETED');

-- Fill in OrderItems table
INSERT INTO OrderItems (order_id, cart_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4);
