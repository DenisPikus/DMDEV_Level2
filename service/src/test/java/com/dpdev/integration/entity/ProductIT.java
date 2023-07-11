package com.dpdev.integration.entity;

import com.dpdev.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.dpdev.util.HibernateUtil.buildSessionFactory;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class ProductIT {

    private Session session = null;

    @Test
    void saveProduct() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            Product expectedProduct = Product.builder()
                    .name("Jerkbait")
                    .brandId(1L)
                    .description("")
                    .price(BigDecimal.valueOf(2.5))
                    .availability(true)
                    .photoPath(null)
                    .build();

            session.beginTransaction();
            Long id = (Long) session.save(expectedProduct);
            session.getTransaction().commit();
            log.info("Expected product {} was saved in to DB with id = {}", expectedProduct, id);

            session.beginTransaction();
            Product actualProduct = session.get(Product.class, id);
            session.getTransaction().commit();
            expectedProduct.setId(id);

            assertThat(actualProduct).isEqualTo(expectedProduct);
            log.info("Actual product {} is equal to expected product {}", actualProduct, expectedProduct);
        }
    }

    @Test
    void getProduct() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            session.beginTransaction();
            Product actualProduct = session.get(Product.class, 3L);
            session.getTransaction().commit();

            assertThat(actualProduct.getName()).isEqualTo("Jig");
            assertThat(actualProduct.getBrandId()).isEqualTo(3);
            assertThat(actualProduct.getDescription()).isEqualTo("A weighted lure with a soft plastic or feather tail.");
            assertThat(actualProduct.getPrice()).isEqualTo(BigDecimal.valueOf(5.99));
            assertThat(actualProduct.getAvailability()).isEqualTo(false);
            log.info("Actual Product {} is equal to expected Product {}", actualProduct.getName(), "Shimano");
        }
    }
}
