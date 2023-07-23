package com.dpdev.integration.entity;

import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StockIT extends IntegrationTestBase {

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        TestDataImporter.importData(entityManager);
    }

    @Test
    void savePositionToStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .productType(ProductType.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        entityManager.persist(product);
        entityManager.persist(expectedStock);
        entityManager.flush();
        entityManager.clear();

        Long actualId = expectedStock.getId();

        assertThat(actualId).isNotNull();
    }

    @Test
    void getPositionFromStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .productType(ProductType.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        entityManager.persist(product);
        entityManager.persist(expectedStock);
        entityManager.flush();
        entityManager.clear();

        Stock actualStock = entityManager.find(Stock.class, expectedStock.getId());

        assertThat(actualStock).isEqualTo(expectedStock);
    }

    @Test
    void updatePositionInStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .productType(ProductType.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        entityManager.persist(product);
        entityManager.persist(expectedStock);
        entityManager.flush();
        entityManager.clear();

        expectedStock.setQuantity(10);
        expectedStock.setAddress("BY, Gomel");
        entityManager.merge(expectedStock);
        entityManager.flush();
        entityManager.clear();

        Stock actualStock = entityManager.find(Stock.class, expectedStock.getId());
        assertThat(actualStock).isEqualTo(expectedStock);
    }

    @Test
    void deletePositionFromStock() {
        Product product = Product.builder()
                .name("Casting Rod")
                .brand(Brand.GANCRAFT)
                .productType(ProductType.ROD)
                .description("Casting rod")
                .price(new BigDecimal("200.00"))
                .availability(true)
                .build();
        Stock expectedStock = createStock(product);
        entityManager.persist(product);
        entityManager.persist(expectedStock);
        entityManager.flush();
        entityManager.clear();

        entityManager.remove(expectedStock);
        entityManager.flush();

        Stock actualStock = entityManager.find(Stock.class, expectedStock.getId());
        assertThat(actualStock).isNull();
    }
}
