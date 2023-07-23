package com.dpdev.integration.dao;

import com.dpdev.dao.ProductRepository;
import com.dpdev.dto.ProductFilter;
import com.dpdev.entity.Product;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryIT extends IntegrationTestBase {
    private ProductRepository productRepository;
    private Product actualProduct;

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        productRepository = new ProductRepository(entityManager);
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveProduct() {
        Product product = createProduct();

        productRepository.save(product);

        assertThat(product.getId()).isNotNull();
    }

    @Test
    void updateProduct() {
        Product product = createProduct();
        productRepository.save(product);
        product.setBrand(Brand.MEGABASS);
        product.setAvailability(false);

        productRepository.update(product);

        actualProduct = productRepository.findById(product.getId()).get();
        assertThat(actualProduct).isEqualTo(product);
    }

    @Test
    void delete() {
        Product product = createProduct();
        productRepository.save(product);

        productRepository.delete(product.getId());

        assertThat(productRepository.findById(product.getId())).isNotPresent();
    }

    @Test
    void findById() {
        Product expectedProduct = createProduct();
        productRepository.save(expectedProduct);

        Optional<Product> maybeActualProduct = productRepository.findById(expectedProduct.getId());

        assertThat(maybeActualProduct).isPresent();
        assertThat(maybeActualProduct.get()).isEqualTo(expectedProduct);
    }

    @Test
    void findAll() {
        List<Product> expectedResult = productRepository.findAll();

        assertThat(expectedResult).hasSize(13);
    }

    @Test
    void findProductByBrandAndPrice() {
        ProductFilter filter = ProductFilter.builder()
                .brand(Brand.SHIMANO)
                .minPrice(new BigDecimal("100.00"))
                .maxPrice(new BigDecimal("400.00"))
                .build();

        List<Product> actualProducts = productRepository.findProductByBrandAndTypeAndPrice(filter);

        assertThat(actualProducts).hasSize(2);
    }

    @Test
    void findProductByBrandAndTypeAndPrice() {
        ProductFilter filter = ProductFilter.builder()
                .brand(Brand.MEGABASS)
                .productType(ProductType.BAIT)
                .minPrice(new BigDecimal("20.00"))
                .build();

        List<Product> actualProducts = productRepository.findProductByBrandAndTypeAndPrice(filter);

        assertThat(actualProducts).hasSize(1);
    }
}
