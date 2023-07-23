package com.dpdev.integration.entity;

import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderProductIT extends IntegrationTestBase {

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        expectedOrderProduct.setOrder(order);
        entityManager.persist(user);
        entityManager.persist(product);
        entityManager.persist(order);
        entityManager.persist(expectedOrderProduct);
        entityManager.flush();
        entityManager.clear();

        Long actualId = expectedOrderProduct.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        expectedOrderProduct.setOrder(order);
        entityManager.persist(user);
        entityManager.persist(product);
        entityManager.persist(order);
        entityManager.persist(expectedOrderProduct);
        entityManager.flush();
        entityManager.clear();

        OrderProduct actualOrderProduct = entityManager.find(OrderProduct.class, expectedOrderProduct.getId());

        assertThat(actualOrderProduct).isEqualTo(expectedOrderProduct);
    }

    @Test
    void updateOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        expectedOrderProduct.setOrder(order);
        entityManager.persist(user);
        entityManager.persist(product);
        entityManager.persist(order);
        entityManager.persist(expectedOrderProduct);
        entityManager.flush();
        entityManager.clear();
        Product newProduct = Product.builder()
                .name("GAN CRAFT JOINTED CLAW")
                .brand(Brand.GANCRAFT)
                .productType(ProductType.BAIT)
                .description("Length: 70mm (2.75\n Weight: \n Floater: 4.1g")
                .price(new BigDecimal("50.00"))
                .availability(true)
                .photoPath(null)
                .build();
        entityManager.persist(newProduct);
        expectedOrderProduct.setProduct(newProduct);
        expectedOrderProduct.setPrice(newProduct.getPrice());
        entityManager.merge(expectedOrderProduct);
        entityManager.flush();
        entityManager.clear();

        OrderProduct actualOrderProduct = entityManager.find(OrderProduct.class, expectedOrderProduct.getId());

        assertThat(actualOrderProduct).isEqualTo(expectedOrderProduct);
    }

    @Test
    void deleteOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        expectedOrderProduct.setOrder(order);
        entityManager.persist(user);
        entityManager.persist(product);
        entityManager.persist(order);
        entityManager.persist(expectedOrderProduct);
        entityManager.flush();
        entityManager.clear();
        OrderProduct savedOrderProduct = entityManager.find(OrderProduct.class, expectedOrderProduct.getId());
        entityManager.flush();

        entityManager.remove(savedOrderProduct);
        entityManager.flush();

        OrderProduct actualOrderProduct = entityManager.find(OrderProduct.class, expectedOrderProduct.getId());
        assertThat(actualOrderProduct).isNull();
    }
}
