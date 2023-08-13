--liquibase formatted sql

--changeset dpikus:1
INSERT INTO users (firstname, lastname, email, password, phone_number, address, role)
VALUES ('Ivan', 'Ivanov', 'ivan@gmail.com', 'pass', '1234567890', 'BY, Minsk, 123 Sovetskaja St', 'USER'),
       ('Sergey', 'Sergeev', 'sergey@gmail.com', 'pass', '9876543210', 'BY, Minsk, 121 Sovetskaja St', 'USER'),
       ('Viktor', 'Pupkin', 'viktor@gmail.com', 'pass', '5555555555', 'BY, Minsk, 1 Sovetskaja St', 'ADMIN');

--changeset dpikus:2
INSERT INTO product (name, brand, product_type, description, price, availability, photo_path)
VALUES ('Spinnerbait', 'SHIMANO', 'BAIT', 'A versatile lure with spinning blades.', 9.99, true, 'basePath'),
       ('Crankbait', 'MEGABASS', 'BAIT', 'A lure with a diving bill for deep fishing.', 7.99, true, 'basePath'),
       ('Jig', 'ZIPBAITS', 'BAIT', 'A weighted lure with a soft plastic or feather tail.', 5.99, false, 'basePath'),
('Excense DC', 'SHIMANO', 'REEL', 'Baitcasting reel with electronic break system, weight 200g.', 395.00, true, 'basePath'),
('Scorpion DC', 'SHIMANO', 'REEL', 'Baitcasting reel with electronic break system, weight 180g.', 250.00, true, 'basePath'),
('Rhodium 63L', 'MEGABASS', 'REEL', 'Baitcasting reel, weight 200g.', 350.00, true, 'basePath');

--changeset dpikus:3
INSERT INTO stock (product_id, quantity, address)
VALUES (1, 100, 'BY, Minsk'),
       (2, 100, 'BY, Minsk'),
       (3, 0, 'BY, Minsk');

--changeset dpikus:4
INSERT INTO orders (user_id, creation_date, closing_date, order_status)
VALUES (1, '2023-07-01', null, 'PROCESSING'),
       (2, '2023-06-30', '2023-06-30', 'COMPLETED');

--changeset dpikus:5
INSERT INTO orders_product (order_id, product_id, quantity, price)
VALUES (1, 1, 1, 9.99),
       (1, 2, 2, 7.99),
       (2, 3, 1, 5.99),
       (2, 1, 3, 9.99);
