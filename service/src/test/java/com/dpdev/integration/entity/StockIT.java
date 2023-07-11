package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.Type;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StockIT extends IntegrationTestBase {

    @Test
    void savePositionToStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .type(Type.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();

        Long actualId = expectedStock.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getPositionFromStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .type(Type.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();

        Stock actualStock = session.get(Stock.class, expectedStock.getId());

        assertThat(actualStock).isEqualTo(expectedStock);
    }

    @Test
    void updatePositionInStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .type(Type.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();

        expectedStock.setQuantity(10);
        expectedStock.setAddress("BY, Gomel");
        session.update(expectedStock);
        session.flush();
        session.clear();

        Stock actualStock = session.get(Stock.class, expectedStock.getId());
        assertThat(actualStock).isEqualTo(expectedStock);
    }

    @Test
    void deletePositionFromStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .type(Type.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        session.beginTransaction();
        session.save(product);
        session.save(expectedStock);
        session.flush();
        session.clear();

        session.delete(expectedStock);
        session.flush();

        Stock actualStock = session.get(Stock.class, expectedStock.getId());
        assertThat(actualStock).isNull();
    }
}
