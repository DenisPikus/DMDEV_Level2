package com.dpdev.dto.filter;

import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class ProductFilter {

    @Builder.Default
    List<Brand> brands = new ArrayList<>();

    @Builder.Default
    List<ProductType> productTypes = new ArrayList<>();

    BigDecimal price;
    BigDecimal minPrice;
    BigDecimal maxPrice;
}
