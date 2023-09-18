-- Fill in users table
INSERT INTO users (id, firstname, lastname, username, password, phone_number, address, role)
VALUES (1, 'Ivan', 'Ivanov', 'ivan@gmail.com', '{noop}pass', '1234567890', 'BY, Minsk, 123 Sovetskaja St', 'USER'),
       (2, 'Sergey', 'Sergeev', 'sergey@gmail.com', '{noop}pass', '9876543210', 'BY, Minsk, 121 Sovetskaja St', 'USER'),
       (3, 'Viktor', 'Pupkin', 'viktor@gmail.com', '{noop}pass', '5555555551', 'BY, Minsk, 1 Sovetskaja St', 'USER'),
       (4, 'Andrey', 'Egorov', 'andrey@gmail.com', '{noop}pass', '9876543211', 'BY, Minsk, 121 Sovetskaja St', 'USER'),
       (5, 'Sveta', 'Egorova', 'sveta@gmail.com', '{noop}pass', '98765432122', 'BY, Minsk, 121 Sovetskaja St', 'USER'),
       (6, 'Viktor', 'Petrov', 'viktorpetrov@gmail.com', '{noop}pass', '5555555555', 'BY, Minsk, 1 Sovetskaja St',
        'ADMIN');

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

-- Fill in product table
INSERT INTO product (id, name, brand, product_type, description, price, quantity, availability, image)
VALUES (1, 'Cardiff', 'SHIMANO', 'BAIT', 'Metal spoon, weight 10gr, length 8cm.', 10.00, 100, true, null),
       (2, 'SPOON-X', 'MEGABASS', 'BAIT', 'Metal spoon, weight 8gr, length 5cm.', 8.00, 100, true, null),
       (3, 'Timon', 'JACKALL', 'BAIT', 'Metal spoon, weight 2.7gr, length 4cm.', 5.00, 0, false, null),
       (4, 'Orbit 80', 'ZIPBAITS', 'BAIT', 'Plastic jerkbait, weight 18gr, length 8cm.', 20.00, 50, true, null),
       (5, 'MagSquad 128', 'JACKALL', 'BAIT', 'Plastic jerkbait, weight 21gr, length 13cm.', 25.00, 50, true, null),
       (6, 'Rigge 90', 'ZIPBAITS', 'BAIT', 'Plastic jerkbait, weight 12gr, length 9cm.', 15.00, 70, true, null),
       (7, 'One TenMagnum', 'MEGABASS', 'BAIT', 'Plastic jerkbait, weight 20gr, length 14cm.', 25.00, 70, true, null),
       (8, 'Rest 128', 'GANCRAFT', 'BAIT', 'Plastic jerkbait, weight 21gr, length 12.8cm.', 35.00, 70, true, null),
       (9, 'Excense DC', 'SHIMANO', 'REEL', 'Baitcasting reel with electronic break system, weight 200g.', 495.00, 5,
        true,
        null),
       (10, 'Scorpion DC', 'SHIMANO', 'REEL', 'Baitcasting reel with electronic break system, weight 180g.', 250.00, 5,
        true, null),
       (11, 'Rhodium 63L', 'MEGABASS', 'REEL', 'Baitcasting reel, weight 200g.', 350.00, true, 5, null),
       (12, 'Megabass Destroyer 2020 F1.1 / 2-72Xs', 'MEGABASS', 'ROD',
        'Length: 2.13m, Power: LIGHT, Line Weight: 3-6LB, Lure Weight: 2.5-12.5g, TAPER: FAST, Weight: 97g.', 500.00, 5,
        true, null),
       (13, 'Shimano Scorpion 1600SS-2', 'SHIMANO', 'ROD',
        'Length: 1.83m, Weight: 115g, Power: ULTRA LIGHT, Lure Weight: 3.5-14g, Line Weight: 6-16lb.', 350.00, 3, true,
        null);

SELECT SETVAL('product_id_seq', (SELECT MAX(id) FROM product));

-- Fill in orders table
INSERT INTO orders (id, user_id, creation_date, closing_date, order_status)
VALUES (1, 1, '2023-06-30', '2023-06-30', 'COMPLETED'),
       (2, 1, '2023-07-01', null, 'PROCESSING'),
       (3, 2, '2023-07-02', null, 'PROCESSING'),
       (4, 3, '2023-08-01', '2023-08-05', 'COMPLETED'),
       (5, 4, '2023-08-01', '2023-08-05', 'COMPLETED'),
       (6, 4, '2023-08-03', null, 'PROCESSING'),
       (7, 5, '2023-08-05', null, 'PROCESSING');

SELECT SETVAL('orders_id_seq', (SELECT MAX(id) FROM orders));

-- Fill in orders_product table
INSERT INTO order_item (id, order_id, product_id, quantity, price)
VALUES (1, 1, 1, 2, (SELECT price FROM product WHERE product.id = 1)),
       (2, 1, 4, 2, (SELECT price FROM product WHERE product.id = 4)),
       (3, 1, 8, 1, (SELECT price FROM product WHERE product.id = 8)),
       (4, 2, 10, 1, (SELECT price FROM product WHERE product.id = 10)),
       (5, 2, 13, 1, (SELECT price FROM product WHERE product.id = 13)),
       (6, 3, 4, 2, (SELECT price FROM product WHERE product.id = 4)),
       (7, 3, 5, 1, (SELECT price FROM product WHERE product.id = 5)),
       (8, 3, 7, 3, (SELECT price FROM product WHERE product.id = 7)),
       (9, 4, 6, 2, (SELECT price FROM product WHERE product.id = 6)),
       (10, 4, 8, 1, (SELECT price FROM product WHERE product.id = 8)),
       (11, 4, 9, 1, (SELECT price FROM product WHERE product.id = 9)),
       (12, 5, 1, 10, (SELECT price FROM product WHERE product.id = 1)),
       (13, 5, 2, 10, (SELECT price FROM product WHERE product.id = 2)),
       (14, 6, 4, 1, (SELECT price FROM product WHERE product.id = 4)),
       (15, 6, 5, 1, (SELECT price FROM product WHERE product.id = 5)),
       (16, 6, 6, 1, (SELECT price FROM product WHERE product.id = 6)),
       (17, 6, 12, 1, (SELECT price FROM product WHERE product.id = 12)),
       (18, 7, 11, 2, (SELECT price FROM product WHERE product.id = 11)),
       (19, 7, 12, 2, (SELECT price FROM product WHERE product.id = 12));

SELECT SETVAL('order_item_id_seq', (SELECT MAX(id) FROM order_item));

