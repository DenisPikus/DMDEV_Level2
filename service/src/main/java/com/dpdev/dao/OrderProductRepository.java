package com.dpdev.dao;

import com.dpdev.entity.OrderProduct;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class OrderProductRepository extends BaseRepository<Long, OrderProduct> {

    public OrderProductRepository(EntityManager entityManager) {
        super(OrderProduct.class, entityManager);
    }
}
