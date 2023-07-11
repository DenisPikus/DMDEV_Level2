package com.dpdev.integration.entity;

import com.dpdev.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductIT extends IntegrationTestBase {

    @Test
    void saveProduct() {
        Product expectedProduct = createProduct();
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();

        Long actualId = expectedProduct.getId();
        assertThat(actualId).isNotNull();
    }

    @Test
    void getProduct() {
        Product expectedProduct = createProduct();
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();

        Product actualProduct = session.get(Product.class, expectedProduct.getId());

        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    void updateProduct() {
        Product expectedProduct = createProduct();
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();
        expectedProduct.setPrice(new BigDecimal("200.00"));

        session.update(expectedProduct);
        session.flush();
        session.clear();

        Product actualProduct = session.get(Product.class, expectedProduct.getId());
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    void deleteProduct() {
        Product expectedProduct = createProduct();
        session.beginTransaction();
        session.save(expectedProduct);
        session.flush();
        session.clear();
        Product savedProduct = session.get(Product.class, expectedProduct.getId());
        session.flush();

        session.delete(savedProduct);
        session.flush();

        Product actualProduct = session.get(Product.class, savedProduct.getId());
        assertThat(actualProduct).isNull();
    }
}
