package com.dpdev.integration.entity;

import com.dpdev.entity.Order;
import com.dpdev.entity.OrderStatus;
import com.dpdev.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderIT extends IntegrationTestBase {
    @Test
    void saveOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();

        Long actualId = expectedOrder.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();

        Order actualOrder = session.get(Order.class, expectedOrder.getId());

        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void updateOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();
        expectedOrder.setOrderStatus(OrderStatus.COMPLETED);
        expectedOrder.setClosingDate(Instant.parse("2023-07-10T10:15:30.00Z"));

        session.update(expectedOrder);
        session.flush();
        session.clear();

        Order actualOrder = session.get(Order.class, expectedOrder.getId());
        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void deleteOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();

        session.delete(expectedOrder);

        Order actualOrder = session.get(Order.class, expectedOrder.getId());
        assertThat(actualOrder).isNull();
    }
}
