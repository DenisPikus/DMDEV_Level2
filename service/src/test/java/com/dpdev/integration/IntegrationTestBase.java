package com.dpdev.integration;

import com.dpdev.entity.OrderProduct;
import com.dpdev.entity.Orders;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import com.dpdev.entity.enums.Brand;
import com.dpdev.entity.enums.OrderStatus;
import com.dpdev.entity.enums.ProductType;
import com.dpdev.entity.enums.Role;
import com.dpdev.integration.util.TestDataImporter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;

import static com.dpdev.integration.util.ContainerTestUtil.postgres;

@SpringBootTest()
@Transactional
public abstract class IntegrationTestBase {

    @Autowired
    protected EntityManager entityManager;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void startSession() {
        TestDataImporter.importData(entityManager);
    }

    public static User createUser() {
        return User.builder()
                .firstname("User")
                .lastname("User")
                .email("user@gmail.com")
                .password("pass")
                .phoneNumber("511112111")
                .address("BY, Minsk, 300 Sovetskaja St")
                .role(Role.USER)
                .build();
    }

    public static Product createProduct() {
        return Product.builder()
                .name("Calcutta Conquest DC 200")
                .brand(Brand.SHIMANO)
                .productType(ProductType.REEL)
                .description("Top baitcasting reel for finesse.")
                .price(BigDecimal.valueOf(499.99))
                .availability(true)
                .photoPath(null)
                .build();
    }

    public static Stock createStock(Product product) {
        Stock stock = Stock.builder()
                .product(product)
                .quantity(200)
                .build();
        stock.setProduct(product);
        return stock;
    }

    public static OrderProduct createOrderProduct(Orders order, Product product) {
        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .quantity(2)
                .product(product)
                .price(product.getPrice())
                .build();
        orderProduct.setOrders(order);
        return orderProduct;
    }

    public static Orders createOrder(User user) {
        return Orders.builder()
                .user(user)
                .creationDate(Instant.parse("2023-07-08T10:15:30.00Z"))
                .orderStatus(OrderStatus.PROCESSING)
                .build();
    }
}
