package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
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
public class BrandIT {

    private Session session = null;

    @Test
    void saveBrand() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            Brand expectedBrand = Brand.builder()
                    .name("GanCraft")
                    .build();

            session.beginTransaction();
            Long id = (Long) session.save(expectedBrand);
            session.getTransaction().commit();
            log.info("Expected brand {} was saved in to DB with id = {}", expectedBrand, id);

            session.beginTransaction();
            Brand actualBrand = session.get(Brand.class, id);
            session.getTransaction().commit();
            expectedBrand.setId(id);

            assertThat(actualBrand).isEqualTo(expectedBrand);
            log.info("Actual brand {} is equal to expected brand {}", actualBrand, expectedBrand);
        }
    }

    @Test
    void getBrand() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            session.beginTransaction();
            Brand actualBrand = session.get(Brand.class, 1L);
            session.getTransaction().commit();

            assertThat(actualBrand.getName()).isEqualTo("Shimano");
            log.info("Actual brand {} is equal to expected brand {}", actualBrand.getName(), "Shimano");
        }
    }
}
