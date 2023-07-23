package com.dpdev.integration.dao;

import com.dpdev.dao.ProductRepository;
import com.dpdev.dao.StockRepository;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.integration.util.TestDataImporter;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class StockRepositoryIT extends IntegrationTestBase {

    private ProductRepository productRepository;
    private StockRepository stockRepository;

    @BeforeEach
    void startSession() {
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args1) -> method.invoke(getCurrentSessionFactory().getCurrentSession(), args1));
        entityManager.getTransaction().begin();
        productRepository = new ProductRepository(entityManager);
        stockRepository = new StockRepository(entityManager);
        TestDataImporter.importData(entityManager);
    }

    @Test
    void saveStock() {
        Stock stock = createStock(null);

        stockRepository.save(stock);

        assertThat(stock.getId()).isNotNull();
    }

    @Test
    void saveStockWithProduct() {
        Product product = createProduct();
        productRepository.save(product);
        Stock stock = createStock(product);

        stockRepository.save(stock);

        assertThat(product.getId()).isNotNull();
        assertThat(stock.getId()).isNotNull();
    }

    @Test
    void updateStock() {
        Product product = createProduct();
        productRepository.save(product);
        Stock stock = createStock(null);
        stockRepository.save(stock);

        stock.setProduct(product);

        stockRepository.update(stock);

        assertThat(stock.getProduct()).isEqualTo(product);
    }

    @Test
    void delete() {
        Stock stock = createStock(null);
        stockRepository.save(stock);

        stockRepository.delete(stock.getId());

        assertThat(stockRepository.findById(stock.getId())).isNotPresent();
    }

    @Test
    void findById() {
        Stock expectedStock = createStock(null);
        stockRepository.save(expectedStock);

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
