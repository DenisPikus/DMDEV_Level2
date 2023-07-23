package com.dpdev.integration.entity;

import com.dpdev.entity.Product;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductIT extends IntegrationTestBase {

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveProduct() {
        Product expectedProduct = createProduct();

        entityManager.persist(expectedProduct);
        entityManager.flush();
        entityManager.clear();

        Long actualId = expectedProduct.getId();
        assertThat(actualId).isNotNull();
    }

    @Test
    void getProduct() {
        Product expectedProduct = createProduct();
        entityManager.persist(expectedProduct);
        entityManager.flush();
        entityManager.clear();

        Product actualProduct = entityManager.find(Product.class, expectedProduct.getId());

        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    void updateProduct() {
        Product expectedProduct = createProduct();
        entityManager.persist(expectedProduct);
        entityManager.flush();
        entityManager.clear();
        expectedProduct.setPrice(new BigDecimal("200.00"));

        entityManager.merge(expectedProduct);
        entityManager.flush();
        entityManager.clear();

        Product actualProduct = entityManager.find(Product.class, expectedProduct.getId());
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    void deleteProduct() {
        Product expectedProduct = createProduct();
        entityManager.persist(expectedProduct);
        entityManager.flush();
        entityManager.clear();
        Product savedProduct = entityManager.find(Product.class, expectedProduct.getId());
        entityManager.flush();

        entityManager.remove(savedProduct);
        entityManager.flush();

        Product actualProduct = entityManager.find(Product.class, savedProduct.getId());
        assertThat(actualProduct).isNull();
    }
}
