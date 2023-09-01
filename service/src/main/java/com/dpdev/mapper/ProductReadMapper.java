package com.dpdev.mapper;

import com.dpdev.dto.ProductReadDto;
import com.dpdev.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductReadMapper implements Mapper<Product, ProductReadDto> {

    @Override
    public ProductReadDto map(Product object) {
        return ProductReadDto.builder()
                .id(object.getId())
                .name(object.getName())
                .brand(object.getBrand())
                .productType(object.getProductType())
                .description(object.getDescription())
                .price(object.getPrice())
                .availability(object.getAvailability())
                .photoPath(object.getPhotoPath())
                .stockId(object.getStock().getId())
                .build();
    }
}
