package com.dpdev.integration.entity;

import com.dpdev.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserIT extends IntegrationTestBase {

    @Test
    void saveUser() {
        User expectedUser = createUser();
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();

        Long actualId = expectedUser.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getUser() {
        User expectedUser = createUser();
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, expectedUser.getId());

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void updateUser() {
        User expectedUser = createUser();
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();
        expectedUser.setFirstname("Pavel");
        expectedUser.setLastname("Pavlov");

        session.update(expectedUser);
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, expectedUser.getId());
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void deleteUser() {
        User expectedUser = createUser();
        session.beginTransaction();
        session.save(expectedUser);
        session.flush();
        session.clear();

        session.delete(expectedUser);
        session.flush();
        session.clear();

        User actualUser = session.get(User.class, expectedUser.getId());
        assertThat(actualUser).isNull();
    }
}
