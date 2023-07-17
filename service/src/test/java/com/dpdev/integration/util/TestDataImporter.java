package com.dpdev.integration.util;

import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.entity.enums.Role;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.Instant;

@UtilityClass
public class TestDataImporter {

    public void importData(Session session) {
        User billGates = saveUser(session, "Bill", "Gates",
                "billGates@gmail.com", "pass", "1111111",
                "USA, NY", Role.USER);
        User steveJobs = saveUser(session, "Steve", "Jobs",
                "steveJobs@gmail.com", "pass", "2222222",
                "USA, NY", Role.USER);
        User sergeyBrin = saveUser(session, "Sergey", "Brin",
                "sergeyBrin@gmail.com", "pass", "3333333",
                "USA, DC", Role.USER);
        User timCook = saveUser(session, "Tim", "Cook",
                "timCook@gmail.com", "pass", "4444444",
                "USA, NY", Role.ADMIN);

        Product spoon = saveProduct(session, "Cardiff", Brand.SHIMANO,
                ProductType.BAIT, "Metal spoon, weight 10gr, length 8cm",
                new BigDecimal("10.00"), true, "basePath");
        Product spoon1 = saveProduct(session, "SPOON-X", Brand.MEGABASS,
                ProductType.BAIT, "Metal spoon, weight 8gr, length 5cm",
                new BigDecimal("15.00"), true, "basePath");
        Product spoon2 = saveProduct(session, "Timon", Brand.JACKALL,
                ProductType.BAIT, "Metal spoon, weight 2.7gr, length 4cm",
                new BigDecimal("8.00"), true, "basePath");

        Product jerkBait = saveProduct(session, "Orbit 80", Brand.ZIPBAITS,
                ProductType.BAIT, "Plastic jerkbait, weight 18gr, length 8cm",
                new BigDecimal("20.00"), true, "basePath");
        Product jerkBait1 = saveProduct(session, "MagSquad 128", Brand.JACKALL,
                ProductType.BAIT, "Plastic jerkbait, weight 21gr, length 13cm",
                new BigDecimal("25.00"), true, "basePath");
        Product jerkBait2 = saveProduct(session, "Rigge 90", Brand.ZIPBAITS,
                ProductType.BAIT, "Plastic jerkbait, weight 12gr, length 9cm",
                new BigDecimal("15.00"), true, "basePath");
        Product jerkBait3 = saveProduct(session, "One TenMagnum", Brand.MEGABASS,
                ProductType.BAIT, "Plastic jerkbait, weight 20gr, length 14cm",
                new BigDecimal("25.00"), true, "basePath");
        Product jerkBait4 = saveProduct(session, "Rest 128", Brand.GANCRAFT,
                ProductType.BAIT, "Plastic jerkbait, weight 21gr, length 12.8cm",
                new BigDecimal("35.00"), false, "basePath");

        Product reel = saveProduct(session, "Excense DC", Brand.SHIMANO,
                ProductType.REEL, "Baitcasting reel with electronic break system, weight 200g",
                new BigDecimal("495.00"), true, "basePath");
        Product reel1 = saveProduct(session, "Scorpion", Brand.SHIMANO,
                ProductType.REEL, "Baitcasting reel with electronic break system, weight 180g",
                new BigDecimal("250.00"), true, "basePath");
        Product reel2 = saveProduct(session, "Rhodium 63L", Brand.MEGABASS,
                ProductType.REEL, "Baitcasting reel, weight 200g",
                new BigDecimal("350.00"), true, "basePath");

        Product rod = saveProduct(session, "Megabass Destroyer 2020 F1.1 / 2-72Xs", Brand.MEGABASS,
                ProductType.ROD, "Length: 2.13m, Power: LIGHT, Line Weight: 3-6LB, " +
                        "Lure Weight: 2.5-12.5g, TAPER: FAST, Weight: 97g",
                new BigDecimal("500.00"), true, "basePath");
        Product rod1 = saveProduct(session, "Shimano Scorpion 1600SS-2", Brand.SHIMANO,
                ProductType.ROD, "Length: 1.83m, Weight: 115g, Power: ULTRA LIGHT, " +
                        "Lure Weight: 3.5-14g, Line Weight: 6-16lb",
                new BigDecimal("350.00"), true, "basePath");

        saveStock(session, spoon, 100);
        saveStock(session, spoon1, 100);
        saveStock(session, spoon2, 100);
        saveStock(session, jerkBait, 100);
        saveStock(session, jerkBait1, 200);
        saveStock(session, jerkBait2, 10);
        saveStock(session, jerkBait3, 50);
        saveStock(session, jerkBait4, 0);
        saveStock(session, reel, 5);
        saveStock(session, reel1, 10);
        saveStock(session, reel2, 3);
        saveStock(session, rod, 5);
        saveStock(session, rod1, 2);

        Order order = saveOrder(session, billGates, Instant.now(), null, OrderStatus.PROCESSING);
        Order order1 = saveOrder(session, steveJobs, Instant.now(), null, OrderStatus.PROCESSING);
        Order order2 = saveOrder(session, sergeyBrin, Instant.parse("2023-07-08T10:15:30.00Z"), Instant.now(), OrderStatus.COMPLETED);

        saveOrderProduct(session, order, spoon, 2); //10
        saveOrderProduct(session, order, jerkBait, 5);  //20
        saveOrderProduct(session, order, jerkBait1, 8); //25
        saveOrderProduct(session, order, reel1, 1); //250

        saveOrderProduct(session, order1, jerkBait3, 2);   //25
        saveOrderProduct(session, order1, rod, 1);  //500
        saveOrderProduct(session, order1, reel2, 1);    //350

        saveOrderProduct(session, order2, jerkBait2, 2);    //15
        saveOrderProduct(session, order2, jerkBait1, 3);    //25
        saveOrderProduct(session, order2, reel, 1); //495
    }

    private User saveUser(Session session,
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
        session.save(user);

        return user;
    }

    private Product saveProduct(Session session,
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
        session.save(product);

        return product;
    }

    private void saveStock(Session session,
                           Product product,
                           Integer quantity) {
        Stock stock = Stock.builder()
                .product(product)
                .quantity(quantity)
                .build();
        session.save(stock);
    }

    private Order saveOrder(Session session,
                            User user,
                            Instant creationDate,
                            Instant closingDate,
                            OrderStatus orderStatus) {
        Order order = Order.builder()
                .user(user)
                .creationDate(creationDate)
                .closingDate(closingDate)
                .orderStatus(orderStatus)
                .build();
        session.save(order);

        return order;
    }

    private void saveOrderProduct(Session session,
                                  Order order,
                                  Product product,
                                  Integer quantity) {
        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(quantity)
                .product(product)
                .price(product.getPrice())
                .order(order)
                .build();
        session.save(orderProduct);
    }
}
