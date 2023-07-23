package com.dpdev.dao;

import com.dpdev.dto.ProductFilter;
import com.dpdev.entity.Product;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.integration.util.HibernateTestUtil;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductDaoTest {
    private final ProductDao productDao = ProductDao.getInstance();
    private Session session;

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @BeforeAll
    public void initDb() {
        session = sessionFactory.openSession();
        TestDataImporter.importData(session);
    }

    @AfterAll
    public void finish() {
        session.close();
        sessionFactory.close();
    }

    @Test
    void findProductByBrandAndPrice() {
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .brand(Brand.SHIMANO)
                .minPrice(new BigDecimal("100.00"))
                .maxPrice(new BigDecimal("400.00"))
                .build();

        List<Product> actualProducts = productDao.findProductByBrandAndTypeAndPrice(session, filter);

        assertThat(actualProducts).hasSize(2);

        session.getTransaction().commit();
    }

    @Test
    void findProductByBrandAndTypeAndPrice() {
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .brand(Brand.MEGABASS)
                .productType(ProductType.BAIT)
                .minPrice(new BigDecimal("20.00"))
                .build();

        List<Product> actualProducts = productDao.findProductByBrandAndTypeAndPrice(session, filter);

        assertThat(actualProducts).hasSize(1);

        session.getTransaction().commit();
    }
}
