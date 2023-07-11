package com.dpdev.integration.entity;

import com.dpdev.entity.Stock;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static com.dpdev.util.HibernateUtil.buildSessionFactory;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class StockIT {

    private Session session = null;

    @Test
    void saveStock() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            Stock expectedStock = Stock.builder()
                    .productId(4L)
                    .quantity(200)
                    .build();

            session.beginTransaction();
            Long id = (Long) session.save(expectedStock);
            session.getTransaction().commit();
            log.info("Expected stock {} was saved in to DB with id = {}", expectedStock, id);

            session.beginTransaction();
            Stock actualStock = session.get(Stock.class, id);
            session.getTransaction().commit();
            expectedStock.setId(id);

            assertThat(actualStock).isEqualTo(expectedStock);
            log.info("Actual stock {} is equal to expected stock {}", actualStock, expectedStock);
        }
    }

    @Test
    void getStock() {
        try (SessionFactory sessionFactory = buildSessionFactory()) {
            session = sessionFactory.openSession();

            session.beginTransaction();
            Stock actualStock = session.get(Stock.class, 1L);
            session.getTransaction().commit();

            assertThat(actualStock.getProductId()).isEqualTo(1);
            assertThat(actualStock.getQuantity()).isEqualTo(100);
        }
    }
}
