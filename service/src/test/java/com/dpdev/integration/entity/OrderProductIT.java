package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.Role;
import com.dpdev.entity.Status;
import com.dpdev.entity.Type;
import com.dpdev.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class OrderProductIT extends IntegrationTestBase {
    private User user;
    private Order order;
    private Product product;
    private OrderProduct expectedOrderProduct;

    @BeforeEach
    void setup() {
        session = sessionFactory.openSession();

        user = User.builder()
                .firstname("User")
                .lastname("User" + (new Random().nextInt()))
                .email("user" + (new Random().nextInt()) + "@gmail.com")
                .password("pass")
                .phoneNumber("511112111" + (new Random().nextInt()))
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();

        order = Order.builder()
                .user(user)
                .creationDate(Instant.parse("2023-07-08T10:15:30.00Z"))
                .status(Status.PROCESSING)
                .build();

        product = Product.builder()
                .name("Calcutta Conquest DC 200")
                .brand(Brand.SHIMANO)
                .type(Type.REEL)
                .description("Top baitcasting reel for finesse.")
                .price(new BigDecimal("499.99"))
                .availability(true)
                .photoPath(null)
                .build();


        expectedOrderProduct = OrderProduct.builder()
                .quantity(2)
                .product(product)
                .price(product.getPrice())
                .build();
        expectedOrderProduct.setOrder(order);
    }

    @Test
    void saveOrderProduct() {
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        log.info("Expected OrderProduct {} was saved in to DB with id = {}", expectedOrderProduct, expectedOrderProduct.getId());
        session.clear();

        Long actualId = expectedOrderProduct.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getOrderProduct() {
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        log.info("Expected OrderProduct {} was saved in to DB with id = {}", expectedOrderProduct, expectedOrderProduct.getId());
        session.clear();

        OrderProduct actualOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());

        assertThat(actualOrderProduct).isEqualTo(expectedOrderProduct);
        log.info("Actual OrderProduct {} is equal to expected OrderProduct {}", actualOrderProduct, expectedOrderProduct);
    }

    @Test
    void updateOrderProduct() {
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        log.info("Expected OrderProduct {} was saved in to DB with id = {}", expectedOrderProduct, expectedOrderProduct.getId());
        session.clear();
        Product newProduct = Product.builder()
                .name("GAN CRAFT JOINTED CLAW")
                .brand(Brand.GANCRAFT)
                .type(Type.BAIT)
                .description("Length: 70mm (2.75\n Weight: \n Floater: 4.1g")
                .price(new BigDecimal("50.00"))
                .availability(true)
                .photoPath(null)
                .build();
        session.save(newProduct);
        expectedOrderProduct.setProduct(newProduct);
        expectedOrderProduct.setPrice(newProduct.getPrice());
        session.update(expectedOrderProduct);
        session.flush();
        session.clear();

        OrderProduct actualOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());

        assertThat(actualOrderProduct).isEqualTo(expectedOrderProduct);
        log.info("Actual OrderProduct {} is equal to expected OrderProduct {}", actualOrderProduct, expectedOrderProduct);
    }

    @Test
    void deleteOrderProduct() {
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        log.info("Expected OrderProduct {} was saved in to DB with id = {}", expectedOrderProduct, expectedOrderProduct.getId());
        session.clear();
        OrderProduct savedOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());
        session.flush();

        session.delete(savedOrderProduct);
        session.flush();

        OrderProduct actualOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());
        AssertionsForClassTypes.assertThat(actualOrderProduct).isNull();
        log.info("Product {} was deleted from DB.", actualOrderProduct);
    }
}
