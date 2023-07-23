package com.dpdev.integration.dao;

import com.dpdev.dao.UserRepository;
import com.dpdev.entity.User;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import com.querydsl.core.Tuple;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIT extends IntegrationTestBase {
    private UserRepository userRepository;

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        userRepository = new UserRepository(entityManager);
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveUser() {
        User expectedUser = createUser();

        User actualUser = userRepository.save(expectedUser);

        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void deleteUser() {
        User expectedUser = createUser();
        User actualUser = userRepository.save(expectedUser);
        Long actualUserId = actualUser.getId();

        userRepository.delete(actualUserId);

        assertThat(userRepository.findById(actualUserId)).isNotPresent();
    }

    @Test
    void updateUser() {
        User user = createUser();
        User expectedUser = userRepository.save(user);
        expectedUser.setFirstname("Pavel");
        expectedUser.setLastname("Pavlov");

        userRepository.update(expectedUser);

        Optional<User> actualUser = userRepository.findById(expectedUser.getId());
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get()).isEqualTo(expectedUser);
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(4);
    }

    @Test
    void findById() {
        User user = createUser();
        userRepository.save(user);

        Optional<User> maybeUser = userRepository.findById(user.getId());

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get()).isEqualTo(user);
    }

    @Test
    void findUsersWithAvgOrdersOrderedByUsername() {
        List<Object[]> results = userRepository.findUsersWithAvgOrdersOrderedByEmail();

        assertThat(results).hasSize(3);

        List<String> userEmails = results.stream()
                .map(email -> (String) email[0]).collect(toList());
        assertThat(userEmails).contains("billGates@gmail.com", "sergeyBrin@gmail.com", "steveJobs@gmail.com");

        List<Double> avgOrders = results.stream()
                .map(order -> (Double) order[1]).collect(toList());
        assertThat(avgOrders).contains(142.50,
                300.00,
                200.00);
    }

    @Test
    void findUsersWithAvgOrdersOrderedByEmailUsingQuerydsl() {
        List<Tuple> results = userRepository.findUsersWithAvgOrdersOrderedByEmailUsingQuerydsl();

        assertThat(results).hasSize(3);

        List<String> userEmails = results.stream()
                .map(email -> email.get(0, String.class)).collect(toList());
        assertThat(userEmails).contains("billGates@gmail.com", "sergeyBrin@gmail.com", "steveJobs@gmail.com");

        List<Double> avgOrders = results.stream()
                .map(order -> order.get(1, Double.class)).collect(toList());
        assertThat(avgOrders).contains(142.50,
                300.00,
                200.00);
    }
}
