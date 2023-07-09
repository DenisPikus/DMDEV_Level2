package com.dpdev.integration.entity;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Product;
import com.dpdev.entity.Type;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class ProductIT extends IntegrationTestBase {

    Product expectedProduct;

    @BeforeEach
    void setup() {
        session = sessionFactory.openSession();

        expectedProduct = getExpectedProduct();
    }

    @Test
    void saveProduct() {
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();
        log.info("Expected product was saved in to DB with id = {}", expectedProduct.getId());

        Long actualId = expectedProduct.getId();
        assertThat(actualId).isNotNull();
    }

    @Test
    void getProduct() {
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();
        log.info("Expected product was saved in to DB with id = {}", expectedProduct.getId());

        Product actualProduct = session.get(Product.class, expectedProduct.getId());

        assertThat(actualProduct).isEqualTo(expectedProduct);
        log.info("Actual Product {} is equal to expected Product {}", actualProduct, expectedProduct);
    }

    @Test
    void updateProduct() {
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();
        log.info("Expected product was saved in to DB with id = {}", expectedProduct.getId());
        expectedProduct.setPrice(new BigDecimal("200.00"));

        session.update(expectedProduct);
        session.flush();
        session.clear();

        Product actualProduct = session.get(Product.class, expectedProduct.getId());
        assertThat(actualProduct).isEqualTo(expectedProduct);
        log.info("Actual Product {} is equal to expected Product {}", actualProduct, expectedProduct);
    }

    @Test
    void deleteProduct() {
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();
        log.info("Expected product was saved in to DB with id = {}.", expectedProduct.getId());
        Product savedProduct = session.get(Product.class, expectedProduct.getId());
        session.flush();

        session.delete(savedProduct);
        session.flush();

        Product actualProduct = session.get(Product.class, savedProduct.getId());
        assertThat(actualProduct).isNull();
        log.info("Product {} was deleted from DB.", expectedProduct);
    }

    private static Product getExpectedProduct() {
        return Product.builder()
                .name("Calcutta Conquest DC 200")
                .brand(Brand.SHIMANO)
                .type(Type.REEL)
                .description("Top baitcasting reel for finesse.")
                .price(BigDecimal.valueOf(499.99))
                .availability(true)
                .photoPath(null)
                .build();
    }
}
