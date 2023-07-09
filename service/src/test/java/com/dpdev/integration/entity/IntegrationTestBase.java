package com.dpdev.integration.entity;

import com.dpdev.integration.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class IntegrationTestBase {

    static SessionFactory sessionFactory;
    Session session;

    @BeforeAll
    static void openResources() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @AfterAll
    static void closeResources() {
        sessionFactory.close();
    }

    @AfterEach
    void rollbackSession() {
        session.getTransaction().rollback();
//        session.getTransaction().commit();  //todo modify lines 27-28 to session.getTransaction().rollback().
//        session.close();
    }
}
