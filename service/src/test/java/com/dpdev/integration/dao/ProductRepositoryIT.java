package com.dpdev.integration.dao;

import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.QPredicate;
import com.dpdev.dto.ProductFilter;
import com.dpdev.entity.Product;
import com.dpdev.entity.enums.Brand;
import com.dpdev.integration.IntegrationTestBase;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.dpdev.entity.QProduct.product;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ProductRepositoryIT extends IntegrationTestBase {

    private final ProductRepository productRepository;

    @Test
    void findProductByBrandAndPrice() {
        ProductFilter filter = ProductFilter.builder()
                .brand(Brand.SHIMANO)
                .minPrice(new BigDecimal("100.00"))
                .maxPrice(new BigDecimal("400.00"))
                .build();
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), product.brand::eq)
                .add(filter.getProductType(), product.productType::eq)
                .add(filter.getPrice(), product.price::eq)
                .add(filter.getMinPrice(), product.price::goe)
                .add(filter.getMaxPrice(), product.price::loe)
                .buildAnd();

        Iterable<Product> actualProducts = productRepository.findAll(predicate);

        assertThat(actualProducts).hasSize(2);
    }
}
