package com.dpdev.integration.entity;

import com.dpdev.entity.Role;
import com.dpdev.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.dpdev.util.HibernateUtil.buildSessionFactory;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class UserIT {

    private Session session = null;

    @Test
    void saveUser() {
        User expectedUser = createUser();

        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            session.beginTransaction();
            Long id = (Long) session.save(expectedUser);
            session.getTransaction().commit();
            log.info("Expected user {} was saved in to DB with id = {}", expectedUser, id);

            session.beginTransaction();
            User actualUser = session.get(User.class, id);
            session.getTransaction().commit();
            expectedUser.setId(id);

            assertThat(actualUser).isEqualTo(expectedUser);
            log.info("Actual user {} is equal to expected user {}", actualUser, expectedUser);
        }
    }

    @Test
    void getUser() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            session.beginTransaction();
            User actualUser = session.get(User.class, 1L);
            session.getTransaction().commit();

            assertThat(actualUser.getFirstname()).isEqualTo("Ivan");
            assertThat(actualUser.getLastname()).isEqualTo("Ivanov");
            assertThat(actualUser.getEmail()).isEqualTo("ivan@gmail.com");
            assertThat(actualUser.getPhoneNumber()).isEqualTo("1234567890");
            assertThat(actualUser.getAddress()).isEqualTo("BY, Minsk, 123 Sovetskaja St");
            assertThat(actualUser.getRole()).isEqualTo(Role.USER);
            log.info("Actual user {} is equal to expected user", actualUser);
        }
    }

    @Test
    void deleteUser() {
        User expectedUser = createUser();
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            session.beginTransaction();
            Long id = (Long) session.save(expectedUser);
            log.info("User with id {} was saved in to DB", id);
            session.getTransaction().commit();

            session.beginTransaction();
            expectedUser.setId(id);
            session.delete(expectedUser);
            session.getTransaction().commit();

            session.beginTransaction();
            User actualUser = session.get(User.class, id);
            session.getTransaction().commit();

            assertThat(actualUser).isNull();
            log.info("User with id {} was deleted from DB", id);
        }
    }

    private static User createUser() {
        int count = new Random().nextInt();
        User user = User.builder()
                .firstname("User" + count)
                .lastname("User" + count)
                .email("user" + count + "@gmail.com")
                .password("pass")
                .phoneNumber("511112111" + count)
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
        return user;
    }


}
