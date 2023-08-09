package com.dpdev.integration.dao;

import com.dpdev.dao.UserRepository;
import com.dpdev.entity.User;
import com.dpdev.integration.IntegrationTestBase;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void saveUser() {
        User expectedUser = createUser();

        User actualUser = userRepository.save(expectedUser);
        entityManager.clear();

        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void deleteUser() {
        User expectedUser = createUser();
        User actualUser = userRepository.save(expectedUser);
        Long actualUserId = actualUser.getId();
        entityManager.clear();

        userRepository.delete(actualUser);

        assertThat(userRepository.findById(actualUserId)).isNotPresent();
    }

    @Test
    void updateUser() {
        User user = createUser();
        User expectedUser = userRepository.save(user);
        expectedUser.setFirstname("Pavel");
        expectedUser.setLastname("Pavlov");

        userRepository.update(expectedUser);
        entityManager.flush();
        entityManager.clear();

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
        entityManager.clear();

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
