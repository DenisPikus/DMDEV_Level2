package com.dpdev.dao;

import com.dpdev.dto.ProductFilter;
import com.dpdev.entity.Product;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

import static com.dpdev.entity.QProduct.product;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDao {

    private static final ProductDao INSTANCE = new ProductDao();

    public List<Product> findProductByBrandAndTypeAndPrice(Session session, ProductFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), product.brand::eq)
                .add(filter.getProductType(), product.productType::eq)
                .add(filter.getPrice(), product.price::eq)
                .add(filter.getMinPrice(), product.price::goe)
                .add(filter.getMaxPrice(), product.price::loe)
                .buildAnd();
        return new JPAQuery<Product>(session)
                .select(product)
                .from(product)
                .where(predicate)
                .orderBy(product.price.asc())
                .fetch();
    }

    public static ProductDao getInstance() {
        return INSTANCE;
    }
}
