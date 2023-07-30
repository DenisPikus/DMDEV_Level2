package com.dpdev.integration.dao;

import com.dpdev.dao.ProductRepository;
import com.dpdev.dto.ProductFilter;
import com.dpdev.entity.Product;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryIT extends IntegrationTestBase {

    private ProductRepository productRepository = context.getBean(ProductRepository.class);

    @Test
    void saveProduct() {
        Product product = createProduct();

        productRepository.save(product);
        entityManager.clear();

        assertThat(product.getId()).isNotNull();
    }

    @Test
    void updateProduct() {
        Product product = createProduct();
        productRepository.save(product);
        product.setBrand(Brand.MEGABASS);
        product.setAvailability(false);

        productRepository.update(product);
        entityManager.flush();
        entityManager.clear();

        Product actualProduct = productRepository.findById(product.getId()).get();
        assertThat(actualProduct).isEqualTo(product);
    }

    @Test
    void delete() {
        Product product = createProduct();
        productRepository.save(product);
        entityManager.clear();

        productRepository.delete(product);

        assertThat(productRepository.findById(product.getId())).isNotPresent();
    }

    @Test
    void findById() {
        Product expectedProduct = createProduct();
        productRepository.save(expectedProduct);
        entityManager.clear();

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
