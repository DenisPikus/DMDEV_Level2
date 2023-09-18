package com.dpdev.dto;

import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Value
@Builder
public class ProductCreateEditDto {

    @NotNull
    @NotBlank
    String name;

    @NotNull
    Brand brand;

    @NotNull
    ProductType productType;

    @NotNull
    @Size(max = 255)
    String description;

    @NotNull
    @Positive
    BigDecimal price;

    @NotNull
    @Positive
    Integer quantity;

    @NotNull
    boolean availability;

    MultipartFile image;

//    @NotNull
//    Long stockId;
}
