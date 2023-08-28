package com.dpdev.dto;

import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductReadDto {
    Long id;
    String name;
    Brand brand;
    ProductType productType;
    String description;
    BigDecimal price;
    Boolean availability;
    String photoPath;
    Long stockId;
}
