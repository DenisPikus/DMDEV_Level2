package com.dpdev.integration.entity;

import com.dpdev.entity.Order;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderIT extends IntegrationTestBase {

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        entityManager.persist(user);
        entityManager.persist(expectedOrder);
        entityManager.flush();
        entityManager.clear();

        Long actualId = expectedOrder.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        entityManager.persist(user);
        entityManager.persist(expectedOrder);
        entityManager.flush();
        entityManager.clear();

        Order actualOrder = entityManager.find(Order.class, expectedOrder.getId());

        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void updateOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        entityManager.persist(user);
        entityManager.persist(expectedOrder);
        entityManager.flush();
        entityManager.clear();
        expectedOrder.setOrderStatus(OrderStatus.COMPLETED);
        expectedOrder.setClosingDate(Instant.parse("2023-07-10T10:15:30.00Z"));

        entityManager.merge(expectedOrder);
        entityManager.flush();
        entityManager.clear();

        Order actualOrder = entityManager.find(Order.class, expectedOrder.getId());
        assertThat(actualOrder).isEqualTo(expectedOrder);
    }

    @Test
    void deleteOrder() {
        User user = createUser();
        Order expectedOrder = createOrder(user);
        entityManager.persist(user);
        entityManager.persist(expectedOrder);
        entityManager.flush();
        entityManager.clear();

        entityManager.remove(expectedOrder);

        Order actualOrder = entityManager.find(Order.class, expectedOrder.getId());
        assertThat(actualOrder).isNull();
    }
}
