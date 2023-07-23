package com.dpdev.integration.entity;

import com.dpdev.entity.User;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserIT extends IntegrationTestBase {

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveUser() {
        User expectedUser = createUser();
        entityManager.persist(expectedUser);
        entityManager.flush();
        entityManager.clear();

        Long actualId = expectedUser.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getUser() {
        User expectedUser = createUser();
        entityManager.persist(expectedUser);
        entityManager.flush();
        entityManager.clear();

        User actualUser = entityManager.find(User.class, expectedUser.getId());

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void updateUser() {
        User expectedUser = createUser();
        entityManager.persist(expectedUser);
        entityManager.flush();
        entityManager.clear();
        expectedUser.setFirstname("Pavel");
        expectedUser.setLastname("Pavlov");

        entityManager.merge(expectedUser);
        entityManager.flush();
        entityManager.clear();

        User actualUser = entityManager.find(User.class, expectedUser.getId());
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void deleteUser() {
        User expectedUser = createUser();
        entityManager.persist(expectedUser);
        entityManager.flush();
        entityManager.clear();

        entityManager.remove(expectedUser);
        entityManager.flush();
        entityManager.clear();

        User actualUser = entityManager.find(User.class, expectedUser.getId());
        assertThat(actualUser).isNull();
    }
}
