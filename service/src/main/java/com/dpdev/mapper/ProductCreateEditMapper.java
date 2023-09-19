package com.dpdev.mapper;

import com.dpdev.dto.ProductCreateEditDto;
import com.dpdev.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class ProductCreateEditMapper implements Mapper<ProductCreateEditDto, Product> {

    @Override
    public Product map(ProductCreateEditDto object) {
        Product product = new Product();
        copy(object, product);
        return product;
    }

    @Override
    public Product map(ProductCreateEditDto fromObject, Product toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(ProductCreateEditDto object, Product product) {
        product.setName(object.getName());
        product.setBrand(object.getBrand());
        product.setProductType(object.getProductType());
        product.setDescription(object.getDescription());
        product.setPrice(object.getPrice());
        product.setAvailability(object.isAvailability());

        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> product.setImage(image.getOriginalFilename()));
    }
}
