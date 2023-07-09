package com.dpdev.integration.entity;

import com.dpdev.entity.Role;
import com.dpdev.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class UserIT extends IntegrationTestBase {

    private User expectedUser;

    @BeforeEach
    void setup() {
        session = sessionFactory.openSession();

        expectedUser = createUser();
    }

    @Test
    void saveUser() {
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();
        log.info("Expected user {} was saved in to DB.", expectedUser);

        Long actualId = expectedUser.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getUser() {
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();
        log.info("Expected user {} was saved in to DB.", expectedUser);

        User actualUser = session.get(User.class, expectedUser.getId());

        assertThat(actualUser).isEqualTo(expectedUser);
        log.info("Actual user {} is equal to expected user {}", actualUser, expectedUser);
    }

    @Test
    void updateUser() {
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();
        log.info("Expected user {} was saved in to DB.", expectedUser);
        expectedUser.setFirstname("Pavel");
        expectedUser.setLastname("Pavlov");

        session.update(expectedUser);
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, expectedUser.getId());
        assertThat(actualUser).isEqualTo(expectedUser);
        log.info("Actual user {} is equal to expected user {}", actualUser, expectedUser);
    }

    @Test
    void deleteUser() {
        session.beginTransaction();
        session.save(expectedUser);
        log.info("User with id {} was saved in to DB", expectedUser.getId());
        session.flush();
        session.clear();
        User savedlUser = session.get(User.class, expectedUser.getId());
        session.flush();
        session.clear();

        session.delete(expectedUser);
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, expectedUser.getId());
        assertThat(actualUser).isNull();
        log.info("User {} was deleted from DB", expectedUser);
    }

    private static User createUser() {
        int count = new Random().nextInt();
        return User.builder()
                .firstname("User" + count)
                .lastname("User" + count)
                .email("user" + count + "@gmail.com")
                .password("pass")
                .phoneNumber("511112111" + count)
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
    }
}
