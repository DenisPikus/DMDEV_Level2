package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.Type;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class StockIT extends IntegrationTestBase {

    private Product product;
    private Stock expectedStock;

    @BeforeEach
    void setup() {
        session = sessionFactory.openSession();

        product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .type(Type.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();

        expectedStock = Stock.builder()
                .product(product)
                .quantity(200)
                .build();
    }

    @Test
    void savePositionToStock() {
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();
        log.info("Expected stock {} was saved in to DB.", expectedStock);

        Long actualId = expectedStock.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getPositionFromStock() {
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();
        log.info("Expected stock {} was saved in to DB.", expectedStock);

        Stock actualStock = session.get(Stock.class, expectedStock.getId());

        assertThat(actualStock).isEqualTo(expectedStock);
        log.info("Actual stock {} is equal to expected stock {}", actualStock, expectedStock);
    }

    @Test
    void updatePositionInStock() {
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();
        log.info("Expected stock {} was saved in to DB.", expectedStock);

        expectedStock.setQuantity(10);
        expectedStock.setAddress("BY, Gomel");
        session.update(expectedStock);
        session.flush();
        session.clear();

        Stock actualStock = session.get(Stock.class, expectedStock.getId());
        assertThat(actualStock).isEqualTo(expectedStock);
        log.info("Actual stock {} is equal to expected stock {}", actualStock, expectedStock);
    }

    @Test
    void deletePositionFromStock() {
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();
        log.info("Expected stock {} was saved in to DB.", expectedStock);

        session.delete(expectedStock);
        session.flush();

        Stock actualStock = session.get(Stock.class, expectedStock.getId());
        assertThat(actualStock).isNull();
        log.info("Stock {} was deleted from DB.", expectedStock);
    }
}
