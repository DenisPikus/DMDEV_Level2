package com.dpdev.dao;

import com.dpdev.entity.Order;
import com.dpdev.integration.util.HibernateTestUtil;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderDaoTest {

    private final OrderDao orderDao = OrderDao.getInstance();
    private Session session;

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @BeforeAll
    public void initDb() {
        session = sessionFactory.openSession();
        TestDataImporter.importData(session);
    }

    @AfterAll
    public void finish() {
        session.close();
        sessionFactory.close();
    }

    @Test
    void findById() {
        session.beginTransaction();

        Order actualOrder = orderDao.findById(session, 1L);

        System.out.println();
        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder.getId()).isEqualTo(1L);
        assertThat(actualOrder.getUser().getEmail()).isEqualTo("billGates@gmail.com");


    }
}
