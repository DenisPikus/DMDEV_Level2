package com.dpdev.integration.dao;

import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.StockRepository;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class StockRepositoryIT extends IntegrationTestBase {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Test
    void saveStock() {
        Product product = createProduct();
        productRepository.save(product);
        Stock stock = createStock(product);

        stockRepository.save(stock);
        entityManager.clear();

        assertThat(product.getId()).isNotNull();
        assertThat(stock.getId()).isNotNull();
        assertThat(product.getStock()).isEqualTo(stock);
    }

    @Test
    void updateStock() {
        Product product = createProduct();
        Product product1 = Product.builder()
                .name("Test")
                .brand(Brand.SHIMANO)
                .productType(ProductType.BAIT)
                .description("Test description")
                .price(new BigDecimal("1.00"))
                .availability(false)
                .photoPath("test")
                .build();
        productRepository.save(product);
        productRepository.save(product1);
        Stock stock = createStock(product);
        stockRepository.save(stock);
        entityManager.clear();
        stock.setProduct(product1);

        stockRepository.update(stock);
        entityManager.flush();
        entityManager.clear();

        assertThat(stock.getProduct()).isEqualTo(product1);
    }

    @Test
    void delete() {
        Product product = createProduct();
        productRepository.save(product);
        Stock stock = createStock(product);
        stockRepository.save(stock);
        entityManager.clear();

        stockRepository.delete(stock);

        assertThat(stockRepository.findById(stock.getId())).isNotPresent();
    }

    @Test
    void findById() {
        Product product = createProduct();
        productRepository.save(product);
        Stock expectedStock = createStock(product);
        stockRepository.save(expectedStock);
        entityManager.clear();

        Optional<Stock> maybeActualStock = stockRepository.findById(expectedStock.getId());

        assertThat(maybeActualStock).isPresent();
        assertThat(maybeActualStock.get()).isEqualTo(expectedStock);
    }

    @Test
    void findAll() {
        List<Stock> stocks = stockRepository.findAll();

        assertThat(stocks).hasSize(13);
    }
}
