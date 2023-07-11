package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.Type;
import com.dpdev.entity.User;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderProductIT extends IntegrationTestBase {

    @Test
    void saveOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(product);
        expectedOrderProduct.setOrder(order);
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        session.clear();

        Long actualId = expectedOrderProduct.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(product);
        expectedOrderProduct.setOrder(order);
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        session.clear();

        OrderProduct actualOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());

        assertThat(actualOrderProduct).isEqualTo(expectedOrderProduct);
    }

    @Test
    void updateOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(product);
        expectedOrderProduct.setOrder(order);
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
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
    }

    @Test
    void deleteOrderProduct() {
        User user = createUser();
        Order order = createOrder(user);
        Product product = createProduct();
        OrderProduct expectedOrderProduct = createOrderProduct(product);
        expectedOrderProduct.setOrder(order);
        session.beginTransaction();
        session.save(user);
        session.save(product);
        session.save(order);
        session.save(expectedOrderProduct);
        session.flush();
        session.clear();
        OrderProduct savedOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());
        session.flush();

        session.delete(savedOrderProduct);
        session.flush();

        OrderProduct actualOrderProduct = session.get(OrderProduct.class, expectedOrderProduct.getId());
        AssertionsForClassTypes.assertThat(actualOrderProduct).isNull();
    }
}
