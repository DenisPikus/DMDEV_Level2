package com.dpdev.integration;

import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Orders;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.entity.enums.Role;
import com.dpdev.integration.util.HibernateTestUtil;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.Instant;

public class IntegrationTestBase {

    private static SessionFactory sessionFactory;
    protected static EntityManager entityManager;

    @BeforeAll
    static void openResources() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }

    @AfterAll
    static void closeResources() {
        sessionFactory.close();
    }

    @BeforeEach
    void startSession() {
        entityManager.getTransaction().begin();
        TestDataImporter.importData(entityManager);
    }

    @AfterEach
    void rollback() {
        entityManager.getTransaction().rollback();
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
        Stock stock = Stock.builder()
                .product(product)
                .quantity(200)
                .build();
        stock.setProduct(product);
        return stock;
    }

    public static OrderProduct createOrderProduct(Orders order, Product product) {
        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .quantity(2)
                .product(product)
                .price(product.getPrice())
                .build();
        orderProduct.setOrders(order);
        return orderProduct;
    }

    public static Orders createOrder(User user) {
        return Orders.builder()
                .user(user)
                .creationDate(Instant.parse("2023-07-08T10:15:30.00Z"))
                .orderStatus(OrderStatus.PROCESSING)
                .build();
    }
}
