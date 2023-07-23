package com.dpdev.dao;

import com.dpdev.entity.User;
import com.dpdev.integration.util.HibernateTestUtil;
import com.dpdev.integration.util.TestDataImporter;
import com.querydsl.core.Tuple;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {

    private final UserDao userDao = UserDao.getInstance();
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
    void findAll() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = userDao.findAll(session);
        assertThat(users).hasSize(4);

        session.getTransaction().commit();
    }

    @Test
    void findById() {
        session.beginTransaction();

        Optional<User> result = userDao.findById(session, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstname()).isEqualTo("Bill");

        session.getTransaction().commit();
    }

    @Test
    void findUsersWithAvgOrdersOrderedByUsername() {
        session.beginTransaction();

        List<Object[]> results = userDao.findUsersWithAvgOrdersOrderedByEmail(session);

        assertThat(results).hasSize(3);

        List<String> userEmails = results.stream()
                .map(email -> (String) email[0]).collect(toList());
        assertThat(userEmails).contains("billGates@gmail.com", "sergeyBrin@gmail.com", "steveJobs@gmail.com");

        List<Double> avgOrders = results.stream()
                .map(order -> (Double) order[1]).collect(toList());
        assertThat(avgOrders).contains(142.50,
                300.00,
                200.00);

        session.getTransaction().commit();
    }

    @Test
    void findUsersWithAvgOrdersOrderedByEmailUsingQuerydsl() {
        session.beginTransaction();

        List<Tuple> results = userDao.findUsersWithAvgOrdersOrderedByEmailUsingQuerydsl(session);

        assertThat(results).hasSize(3);

        List<String> userEmails = results.stream()
                .map(email -> email.get(0, String.class)).collect(toList());
        assertThat(userEmails).contains("billGates@gmail.com", "sergeyBrin@gmail.com", "steveJobs@gmail.com");

        List<Double> avgOrders = results.stream()
                .map(order -> order.get(1, Double.class)).collect(toList());
        assertThat(avgOrders).contains(142.50,
                300.00,
                200.00);

        session.getTransaction().commit();
    }
}
