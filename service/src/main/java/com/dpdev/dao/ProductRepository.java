package com.dpdev.dao;

import com.dpdev.dto.ProductFilter;
import com.dpdev.entity.Product;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.dpdev.entity.QProduct.product;

@Repository
public class ProductRepository extends BaseRepository<Long, Product> {

    public ProductRepository(EntityManager entityManager) {
        super(Product.class, entityManager);
    }

    public List<Product> findProductByBrandAndTypeAndPrice(ProductFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getBrand(), product.brand::eq)
                .add(filter.getProductType(), product.productType::eq)
                .add(filter.getPrice(), product.price::eq)
                .add(filter.getMinPrice(), product.price::goe)
                .add(filter.getMaxPrice(), product.price::loe)
                .buildAnd();
        return new JPAQuery<Product>(getEntityManager())
                .select(product)
                .from(product)
                .where(predicate)
                .orderBy(product.price.asc())
                .fetch();
    }
}
