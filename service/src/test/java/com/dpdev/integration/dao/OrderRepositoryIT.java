package com.dpdev.integration.dao;

import com.dpdev.dao.OrderProductRepository;
import com.dpdev.dao.OrderRepository;
import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.UserRepository;
import com.dpdev.entity.Order;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Product;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryIT extends IntegrationTestBase {

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
        orderProductRepository = new OrderProductRepository(entityManager);
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        userRepository.save(user);

        Order actualOrder = orderRepository.save(expectedOrder);

        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void deleteOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        userRepository.save(user);
        Order actualOrder = orderRepository.save(expectedOrder);

        orderRepository.delete(actualOrder.getId());

        Optional<Order> maybeOrder = orderRepository.findById(actualOrder.getId());
        assertThat(maybeOrder).isNotPresent();
    }

    @Test
    void updateOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        userRepository.save(user);
        Order actualOrder = orderRepository.save(expectedOrder);
        expectedOrder.setOrderStatus(OrderStatus.COMPLETED);
        expectedOrder.setClosingDate(Instant.parse("2023-07-10T10:15:30.00Z"));

        orderRepository.update(expectedOrder);

        Optional<Order> maybeOrder = orderRepository.findById(actualOrder.getId());
        assertThat(maybeOrder.get()).isEqualTo(expectedOrder);
    }

    @Test
    void findById() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        userRepository.save(user);
        orderRepository.save(expectedOrder);

        Optional<Order> maybeOrder = orderRepository.findById(expectedOrder.getId());

        assertThat(maybeOrder.get()).isEqualTo(expectedOrder);
    }

    @Test
    void findAll() {
        List<Order> actualOrders = orderRepository.findAll();

        assertThat(actualOrders).hasSize(3);
    }

    @Test
    void findByIdWithOrderedProducts() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Order expectedOrder = createOrder(user);
        orderRepository.save(expectedOrder);
        OrderProduct orderProduct = createOrderProduct(expectedOrder, product);
        orderProductRepository.save(orderProduct);

        Order actualOrder = orderRepository.findByIdWithOrderedProducts(expectedOrder.getId());

        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder).isEqualTo(expectedOrder);
        assertThat(actualOrder.getUser().getEmail()).isEqualTo("user@gmail.com");

        assertThat(actualOrder.getOrderProducts()).hasSize(1);
    }
}
