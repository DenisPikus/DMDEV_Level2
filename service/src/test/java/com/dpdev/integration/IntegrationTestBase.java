package com.dpdev.integration;

import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.entity.enums.Role;
import com.dpdev.integration.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;

public class IntegrationTestBase {

    private static final SessionFactory SESSION_FACTORY = HibernateTestUtil.buildSessionFactory();
    protected EntityManager entityManager;

    @AfterAll
    static void closeResources() {
        SESSION_FACTORY.close();
    }

    @AfterEach
    void closeSession() {
        entityManager.getTransaction().rollback();
        entityManager.close();
    }

    public static User createUser() {
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

    public static Product createProduct() {
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

    public static Stock createStock(Product product) {
        return Stock.builder()
                .product(product)
                .quantity(200)
                .build();
    }

    public static OrderProduct createOrderProduct(Order order, Product product) {
        return OrderProduct.builder()
                .order(order)
                .quantity(2)
                .product(product)
                .price(product.getPrice())
                .build();
    }

    public static Order createOrder(User user) {
        return Order.builder()
                .user(user)
                .creationDate(Instant.parse("2023-07-08T10:15:30.00Z"))
                .orderStatus(OrderStatus.PROCESSING)
                .build();
    }

    public static SessionFactory getCurrentSessionFactory() {
        return SESSION_FACTORY;
    }
}
