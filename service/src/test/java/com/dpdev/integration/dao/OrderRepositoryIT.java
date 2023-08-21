package com.dpdev.integration.dao;

import com.dpdev.entity.Orders;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.repository.OrderProductRepository;
import com.dpdev.repository.OrderRepository;
import com.dpdev.repository.ProductRepository;
import com.dpdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class OrderRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Test
    void findByIdWithOrderedProducts() {
        Long orderId = 1L;

        Orders actualOrder = orderRepository.findOrdersAndUsersById(orderId);

        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder.getUser().getEmail()).isEqualTo("ivan@gmail.com");
        assertThat(actualOrder.getOrderProducts()).hasSize(3);
        assertThat(actualOrder.getOrderProducts().get(0).getProduct().getName()).isEqualTo("Cardiff");
        assertThat(actualOrder.getOrderProducts().get(1).getProduct().getName()).isEqualTo("Orbit 80");
        assertThat(actualOrder.getOrderProducts().get(2).getProduct().getName()).isEqualTo("Rest 128");
    }
}
