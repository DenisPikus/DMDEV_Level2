package com.dpdev.integration.entity;

import com.dpdev.entity.Order;
import com.dpdev.entity.Role;
import com.dpdev.entity.Status;
import com.dpdev.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class OrderIT extends IntegrationTestBase {

    private Order expectedOrder;
    private User user;

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

        expectedOrder = Order.builder()
                .user(user)
                .creationDate(Instant.parse("2023-07-08T10:15:30.00Z"))
                .status(Status.PROCESSING)
                .build();
    }

    @Test
    void saveOrder() {
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();
        log.info("Expected order {} was saved in to DB.", expectedOrder);

        Long actualId = expectedOrder.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getOrder() {
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();
        log.info("Expected order {} was saved in to DB.", expectedOrder);

        Order actualOrder = session.get(Order.class, expectedOrder.getId());

        assertThat(actualOrder).isEqualTo(expectedOrder);
        log.info("Actual order {} is equal to expected order {}", actualOrder, expectedOrder);
    }

    @Test
    void updateOrder() {
        session.beginTransaction();
        session.save(user);
        session.save(expectedOrder);
        session.flush();
        session.clear();
        log.info("Expected order {} was saved in to DB.", expectedOrder);
        expectedOrder.setStatus(Status.COMPLETED);
        expectedOrder.setClosingDate(Instant.parse("2023-07-10T10:15:30.00Z"));

        session.update(expectedOrder);
        session.flush();
        session.clear();
        log.info("Expected order {} was updated.", expectedOrder);

        Order actualOrder = session.get(Order.class, expectedOrder.getId());
        assertThat(actualOrder).isEqualTo(expectedOrder);
        log.info("Actual order {} is equal to expected expected order {}", actualOrder, expectedOrder);
    }
}
