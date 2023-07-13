package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.OrderStatus;
import com.dpdev.entity.Product;
import com.dpdev.entity.ProductType;
import com.dpdev.entity.Role;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import com.dpdev.integration.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.Instant;

public abstract class IntegrationTestBase {

    static SessionFactory sessionFactory;
    Session session;

    @BeforeAll
    static void openResources() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @AfterAll
    static void closeResources() {
        sessionFactory.close();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
    }

    @AfterEach
    void rollbackSession() {
        session.getTransaction().rollback();
    }

    static User createUser() {
        return User.builder()
                .firstname("User")
                .lastname("User")
                .email("user@gmail.com")
                .password("pass")
                .phoneNumber("511112111")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
    }

    static Product createProduct() {
        return Product.builder()
                .name("Calcutta Conquest DC 200")
                .brand(Brand.SHIMANO)
                .productType(ProductType.REEL)
                .description("Top baitcasting reel for finesse.")
                .price(BigDecimal.valueOf(499.99))
                .availability(true)
                .photoPath(null)
                .build();
    }

    static Stock createStock(Product product) {
        return Stock.builder()
                .product(product)
                .quantity(200)
                .build();
    }

    static OrderProduct createOrderProduct(Product product) {
        return OrderProduct.builder()
                .quantity(2)
                .product(product)
                .price(product.getPrice())
                .build();
    }

    static Order createOrder(User user) {
        return Order.builder()
                .user(user)
                .creationDate(Instant.parse("2023-07-08T10:15:30.00Z"))
                .orderStatus(OrderStatus.PROCESSING)
                .build();
    }
}
