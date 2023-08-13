package com.dpdev.integration.dao;

import com.dpdev.dao.OrderProductRepository;
import com.dpdev.dao.OrderRepository;
import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.UserRepository;
import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Orders;
import com.dpdev.entity.Product;
import com.dpdev.entity.User;
import com.dpdev.integration.IntegrationTestBase;
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
        User user = createUser();
        userRepository.save(user);
        Product product = createProduct();
        productRepository.save(product);
        Orders expectedOrder = createOrder(user);
        orderRepository.save(expectedOrder);
        OrderProduct orderProduct = createOrderProduct(expectedOrder, product);
        orderProductRepository.save(orderProduct);

        Orders actualOrder = orderRepository.findOrdersAndUsersById(expectedOrder.getId());

        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder).isEqualTo(expectedOrder);
        assertThat(actualOrder.getUser().getEmail()).isEqualTo("user@gmail.com");
        assertThat(actualOrder.getOrderProducts()).hasSize(1);
    }
}
