package com.dpdev.integration.repository;

import com.dpdev.dto.ProductReadDto;
import com.dpdev.dto.filter.ProductFilter;
import com.dpdev.entity.enums.Brand;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ProductServiceIT extends IntegrationTestBase {

    private final ProductService productService;

    @Test
    void findProductByBrandAndPrice() {
        ProductFilter filter = ProductFilter.builder()
                .brands(List.of(Brand.SHIMANO, Brand.MEGABASS))
                .minPrice(new BigDecimal("100.00"))
                .maxPrice(new BigDecimal("400.00"))
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductReadDto> actualProducts = productService.findAll(filter, pageable);

        assertThat(actualProducts).hasSize(3);
    }
}
