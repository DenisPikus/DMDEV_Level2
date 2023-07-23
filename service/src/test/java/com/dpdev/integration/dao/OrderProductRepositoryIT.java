package com.dpdev.integration.dao;

import com.dpdev.dao.OrderProductRepository;
import com.dpdev.dao.OrderRepository;
import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.UserRepository;
import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.User;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderProductRepositoryIT extends IntegrationTestBase {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderProductRepository orderProductRepository;

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        userRepository = new UserRepository(entityManager);
        orderRepository = new OrderRepository(entityManager);
        productRepository = new ProductRepository(entityManager);
        orderProductRepository = new OrderProductRepository(entityManager);        TestDataImporter.importData(entityManager);
    }

    @Test
    void save() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Order order = createOrder(user);
        orderRepository.save(order);
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);

        orderProductRepository.save(expectedOrderProduct);

        assertThat(expectedOrderProduct.getId()).isNotNull();
    }

    @Test
    void update() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Order order = createOrder(user);
        orderRepository.save(order);
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        orderProductRepository.save(expectedOrderProduct);
        expectedOrderProduct.setQuantity(1);

        orderProductRepository.update(expectedOrderProduct);

        assertThat(expectedOrderProduct.getQuantity()).isEqualTo(1);
    }

    @Test
    void delete() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Order order = createOrder(user);
        orderRepository.save(order);
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        orderProductRepository.save(expectedOrderProduct);

        orderProductRepository.delete(expectedOrderProduct.getId());

        assertThat(orderProductRepository.findById(expectedOrderProduct.getId())).isNotPresent();
    }

    @Test
    void findById() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Order order = createOrder(user);
        orderRepository.save(order);
        OrderProduct expectedOrderProduct = createOrderProduct(order, product);
        orderProductRepository.save(expectedOrderProduct);

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
