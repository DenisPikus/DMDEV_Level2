package com.dpdev.dto;

import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Value
@Builder
public class ProductCreateEditDto {
    String name;
    Brand brand;
    ProductType productType;
    String description;
    BigDecimal price;
    Boolean availability;
    MultipartFile image;
    Long stockId;
}
