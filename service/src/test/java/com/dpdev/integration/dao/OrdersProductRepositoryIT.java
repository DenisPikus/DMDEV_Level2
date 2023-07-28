package com.dpdev.integration.dao;

import com.dpdev.dao.OrderProductRepository;
import com.dpdev.dao.OrderRepository;
import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.UserRepository;
import com.dpdev.entity.Orders;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.User;
import com.dpdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OrdersProductRepositoryIT extends IntegrationTestBase {

    private UserRepository userRepository = new UserRepository(entityManager);
    private OrderRepository orderRepository = new OrderRepository(entityManager);
    private ProductRepository productRepository = new ProductRepository(entityManager);
    private OrderProductRepository orderProductRepository = new OrderProductRepository(entityManager);

    @Test
    void save() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Orders orders = createOrder(user);
        orderRepository.save(orders);
        OrderProduct expectedOrderProduct = createOrderProduct(orders, product);

        orderProductRepository.save(expectedOrderProduct);
        entityManager.clear();

        assertThat(expectedOrderProduct.getId()).isNotNull();
    }

    @Test
    void update() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Orders orders = createOrder(user);
        orderRepository.save(orders);
        OrderProduct expectedOrderProduct = createOrderProduct(orders, product);
        orderProductRepository.save(expectedOrderProduct);
        expectedOrderProduct.setQuantity(1);

        orderProductRepository.update(expectedOrderProduct);
        entityManager.flush();
        entityManager.clear();

        assertThat(expectedOrderProduct.getQuantity()).isEqualTo(1);
    }

    @Test
    void delete() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Orders orders = createOrder(user);
        orderRepository.save(orders);
        OrderProduct expectedOrderProduct = createOrderProduct(orders, product);
        orderProductRepository.save(expectedOrderProduct);
        entityManager.clear();

        orderProductRepository.delete(expectedOrderProduct);

        assertThat(orderProductRepository.findById(expectedOrderProduct.getId())).isNotPresent();
    }

    @Test
    void findById() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Orders orders = createOrder(user);
        orderRepository.save(orders);
        OrderProduct expectedOrderProduct = createOrderProduct(orders, product);
        orderProductRepository.save(expectedOrderProduct);
        entityManager.clear();

        Optional<OrderProduct>maybeOrderProduct = orderProductRepository.findById(expectedOrderProduct.getId());

        assertThat(maybeOrderProduct).isPresent();
        assertThat(maybeOrderProduct.get()).isEqualTo(expectedOrderProduct);
    }

    @Test
    void findAll() {
        List<OrderProduct> orderProductList = orderProductRepository.findAll();

        assertThat(orderProductList).hasSize(10);
    }
}
