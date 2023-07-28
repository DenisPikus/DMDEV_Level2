package com.dpdev.integration.util;

import com.dpdev.entity.Orders;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.entity.enums.Role;
import lombok.experimental.UtilityClass;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;

@UtilityClass
public class TestDataImporter {

    public void importData(EntityManager entityManager) {

        User billGates = saveUser(entityManager, "Bill", "Gates",
                "billGates@gmail.com", "pass", "1111111",
                "USA, NY", Role.USER);
        User steveJobs = saveUser(entityManager, "Steve", "Jobs",
                "steveJobs@gmail.com", "pass", "2222222",
                "USA, NY", Role.USER);
        User sergeyBrin = saveUser(entityManager, "Sergey", "Brin",
                "sergeyBrin@gmail.com", "pass", "3333333",
                "USA, DC", Role.USER);
        User timCook = saveUser(entityManager, "Tim", "Cook",
                "timCook@gmail.com", "pass", "4444444",
                "USA, NY", Role.ADMIN);

        Product spoon = saveProduct(entityManager, "Cardiff", Brand.SHIMANO,
                ProductType.BAIT, "Metal spoon, weight 10gr, length 8cm",
                new BigDecimal("10.00"), true, "basePath");
        Product spoon1 = saveProduct(entityManager, "SPOON-X", Brand.MEGABASS,
                ProductType.BAIT, "Metal spoon, weight 8gr, length 5cm",
                new BigDecimal("15.00"), true, "basePath");
        Product spoon2 = saveProduct(entityManager, "Timon", Brand.JACKALL,
                ProductType.BAIT, "Metal spoon, weight 2.7gr, length 4cm",
                new BigDecimal("8.00"), true, "basePath");

        Product jerkBait = saveProduct(entityManager, "Orbit 80", Brand.ZIPBAITS,
                ProductType.BAIT, "Plastic jerkbait, weight 18gr, length 8cm",
                new BigDecimal("20.00"), true, "basePath");
        Product jerkBait1 = saveProduct(entityManager, "MagSquad 128", Brand.JACKALL,
                ProductType.BAIT, "Plastic jerkbait, weight 21gr, length 13cm",
                new BigDecimal("25.00"), true, "basePath");
        Product jerkBait2 = saveProduct(entityManager, "Rigge 90", Brand.ZIPBAITS,
                ProductType.BAIT, "Plastic jerkbait, weight 12gr, length 9cm",
                new BigDecimal("15.00"), true, "basePath");
        Product jerkBait3 = saveProduct(entityManager, "One TenMagnum", Brand.MEGABASS,
                ProductType.BAIT, "Plastic jerkbait, weight 20gr, length 14cm",
                new BigDecimal("25.00"), true, "basePath");
        Product jerkBait4 = saveProduct(entityManager, "Rest 128", Brand.GANCRAFT,
                ProductType.BAIT, "Plastic jerkbait, weight 21gr, length 12.8cm",
                new BigDecimal("35.00"), false, "basePath");

        Product reel = saveProduct(entityManager, "Excense DC", Brand.SHIMANO,
                ProductType.REEL, "Baitcasting reel with electronic break system, weight 200g",
                new BigDecimal("495.00"), true, "basePath");
        Product reel1 = saveProduct(entityManager, "Scorpion", Brand.SHIMANO,
                ProductType.REEL, "Baitcasting reel with electronic break system, weight 180g",
                new BigDecimal("250.00"), true, "basePath");
        Product reel2 = saveProduct(entityManager, "Rhodium 63L", Brand.MEGABASS,
                ProductType.REEL, "Baitcasting reel, weight 200g",
                new BigDecimal("350.00"), true, "basePath");

        Product rod = saveProduct(entityManager, "Megabass Destroyer 2020 F1.1 / 2-72Xs", Brand.MEGABASS,
                ProductType.ROD, "Length: 2.13m, Power: LIGHT, Line Weight: 3-6LB, " +
                        "Lure Weight: 2.5-12.5g, TAPER: FAST, Weight: 97g",
                new BigDecimal("500.00"), true, "basePath");
        Product rod1 = saveProduct(entityManager, "Shimano Scorpion 1600SS-2", Brand.SHIMANO,
                ProductType.ROD, "Length: 1.83m, Weight: 115g, Power: ULTRA LIGHT, " +
                        "Lure Weight: 3.5-14g, Line Weight: 6-16lb",
                new BigDecimal("350.00"), true, "basePath");

        saveStock(entityManager, spoon, 100);
        saveStock(entityManager, spoon1, 100);
        saveStock(entityManager, spoon2, 100);
        saveStock(entityManager, jerkBait, 100);
        saveStock(entityManager, jerkBait1, 200);
        saveStock(entityManager, jerkBait2, 10);
        saveStock(entityManager, jerkBait3, 50);
        saveStock(entityManager, jerkBait4, 0);
        saveStock(entityManager, reel, 5);
        saveStock(entityManager, reel1, 10);
        saveStock(entityManager, reel2, 3);
        saveStock(entityManager, rod, 5);
        saveStock(entityManager, rod1, 2);

        Orders orders = saveOrder(entityManager, billGates, Instant.now(), null, OrderStatus.PROCESSING);
        Orders orders1 = saveOrder(entityManager, steveJobs, Instant.now(), null, OrderStatus.PROCESSING);
        Orders orders2 = saveOrder(entityManager, sergeyBrin, Instant.parse("2023-07-08T10:15:30.00Z"), Instant.now(), OrderStatus.COMPLETED);

        saveOrderProduct(entityManager, orders, spoon, 2); //10
        saveOrderProduct(entityManager, orders, jerkBait, 5);  //20
        saveOrderProduct(entityManager, orders, jerkBait1, 8); //25
        saveOrderProduct(entityManager, orders, reel1, 1); //250

        saveOrderProduct(entityManager, orders1, jerkBait3, 2);   //25
        saveOrderProduct(entityManager, orders1, rod, 1);  //500
        saveOrderProduct(entityManager, orders1, reel2, 1);    //350

        saveOrderProduct(entityManager, orders2, jerkBait2, 2);    //15
        saveOrderProduct(entityManager, orders2, jerkBait1, 3);    //25
        saveOrderProduct(entityManager, orders2, reel, 1); //495
    }

    private User saveUser(EntityManager entityManager,
                          String firstName,
                          String lastName,
                          String email,
                          String password,
                          String phoneNumber,
                          String address,
                          Role role) {
        User user = User.builder()
                .firstname(firstName)
                .lastname(lastName)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .address(address)
                .role(role)
                .build();
        entityManager.persist(user);

        return user;
    }

    private Product saveProduct(EntityManager entityManager,
                                String name,
                                Brand brand,
                                ProductType productType,
                                String description,
                                BigDecimal price,
                                Boolean availability,
                                String photoPath) {
        Product product = Product.builder()
                .name(name)
                .brand(brand)
                .productType(productType)
                .description(description)
                .price(price)
                .availability(availability)
                .photoPath(photoPath)
                .build();
        entityManager.persist(product);

        return product;
    }

    private void saveStock(EntityManager entityManager,
                           Product product,
                           Integer quantity) {
        Stock stock = Stock.builder()
                .product(product)
                .quantity(quantity)
                .build();
        entityManager.persist(stock);
    }

    private Orders saveOrder(EntityManager entityManager,
                             User user,
                             Instant creationDate,
                             Instant closingDate,
                             OrderStatus orderStatus) {
        Orders orders = Orders.builder()
                .user(user)
                .creationDate(creationDate)
                .closingDate(closingDate)
                .orderStatus(orderStatus)
                .build();
        entityManager.persist(orders);

        return orders;
    }

    private void saveOrderProduct(EntityManager entityManager,
                                  Orders order,
                                  Product product,
                                  Integer quantity) {
        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(quantity)
                .product(product)
                .price(product.getPrice())
                .order(order)
                .build();
        entityManager.persist(orderProduct);
    }
}
