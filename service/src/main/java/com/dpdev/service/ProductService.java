package com.dpdev.service;

import com.dpdev.dto.ProductCreateEditDto;
import com.dpdev.dto.ProductReadDto;
import com.dpdev.dto.filter.ProductFilter;
import com.dpdev.mapper.ProductCreateEditMapper;
import com.dpdev.mapper.ProductReadMapper;
import com.dpdev.repository.ProductRepository;
import com.dpdev.repository.QPredicate;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dpdev.entity.QProduct.product;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;
    private final ProductReadMapper productReadMapper;
    private final ProductCreateEditMapper productCreateEditMapper;

    public Page<ProductReadDto> findAll(ProductFilter filter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), product.brand::eq)
                .add(filter.getProductType(), product.productType::eq)
                .add(filter.getPrice(), product.price::eq)
                .add(filter.getMinPrice(), product.price::goe)
                .add(filter.getMaxPrice(), product.price::loe)
                .buildAnd();

        return repository.findAll(predicate, pageable)
                .map(productReadMapper::map);
    }

    public Optional<ProductReadDto> findById(Long id) {
        return repository.findById(id)
                .map(productReadMapper::map);
    }

    @Transactional
    public ProductReadDto create(ProductCreateEditDto productDto) {
        return Optional.of(productDto)
                .map(productCreateEditMapper::map)
                .map(repository::save)
                .map(productReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<ProductReadDto> update(Long id, ProductCreateEditDto productDto) {
        return repository.findById(id)
                .map(product -> productCreateEditMapper.map(productDto, product))
                .map(repository::saveAndFlush)
                .map(productReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.findById(id)
                .map(product -> {
                    repository.delete(product);
                    repository.flush();
                    return true;
                })
                .orElse(false);
    }
}
