package com.dpdev.dto.filter;

import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductFilter {
    Brand brand;
    ProductType productType;
    BigDecimal price;
    BigDecimal minPrice;
    BigDecimal maxPrice;
}
