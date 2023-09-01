package com.dpdev.mapper;

import com.dpdev.dto.ProductCreateEditDto;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductCreateEditMapper implements Mapper<ProductCreateEditDto, Product> {

    private final StockRepository stockRepository;

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
        product.setAvailability(object.getAvailability());
        product.setPhotoPath(object.getPhotoPath());
        product.setStock(getStock(object.getStockId()));
    }

    public Stock getStock(Long stockId) {
        return Optional.ofNullable(stockId)
                .flatMap(stockRepository::findById)
                .orElse(null);
    }
}
