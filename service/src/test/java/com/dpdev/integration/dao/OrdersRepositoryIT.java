package com.dpdev.integration.dao;

import com.dpdev.dao.OrderProductRepository;
import com.dpdev.dao.OrderRepository;
import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.UserRepository;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Orders;
import com.dpdev.entity.Product;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class OrdersRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Test
    void saveOrder() {
        User user = createUser();
        userRepository.save(user);
        Orders expectedOrders = createOrder(user);

        Orders actualOrders = orderRepository.save(expectedOrders);
        entityManager.clear();

        assertThat(actualOrders).isNotNull();
        assertThat(actualOrders).isEqualTo(expectedOrders);
    }

    @Test
    void deleteOrder() {
        User user = createUser();
        Orders expectedOrders = createOrder(user);
        userRepository.save(user);
        Orders actualOrders = orderRepository.save(expectedOrders);
        entityManager.clear();

        orderRepository.delete(actualOrders);

        Optional<Orders> maybeOrder = orderRepository.findById(actualOrders.getId());
        assertThat(maybeOrder).isNotPresent();
    }

    @Test
    void updateOrder() {
        User user = createUser();
        Orders expectedOrders = createOrder(user);
        userRepository.save(user);
        Orders actualOrders = orderRepository.save(expectedOrders);
        expectedOrders.setOrderStatus(OrderStatus.COMPLETED);
        expectedOrders.setClosingDate(Instant.parse("2023-07-10T10:15:30.00Z"));

        orderRepository.update(expectedOrders);
        entityManager.flush();
        entityManager.clear();

        Optional<Orders> maybeOrder = orderRepository.findById(actualOrders.getId());
        assertThat(maybeOrder.get()).isEqualTo(expectedOrders);
    }

    @Test
    void findById() {
        User user = createUser();
        Orders expectedOrders = createOrder(user);
        userRepository.save(user);
        orderRepository.save(expectedOrders);
        entityManager.clear();

        Optional<Orders> maybeOrder = orderRepository.findById(expectedOrders.getId());

        assertThat(maybeOrder.get()).isEqualTo(expectedOrders);
    }

    @Test
    void findAll() {
        List<Orders> actualOrders = orderRepository.findAll();

        assertThat(actualOrders).hasSize(3);
    }

    @Test
    void findByIdWithOrderedProducts() {
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Orders expectedOrder = createOrder(user);
        orderRepository.save(expectedOrder);
        OrderProduct orderProduct = createOrderProduct(expectedOrder, product);
        orderProductRepository.save(orderProduct);

        Orders actualOrder = orderRepository.findByIdWithOrderedProducts(expectedOrder.getId());

        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder).isEqualTo(expectedOrder);
        assertThat(actualOrder.getUser().getEmail()).isEqualTo("user@gmail.com");
        assertThat(actualOrder.getOrderProducts()).hasSize(1);
    }
}
