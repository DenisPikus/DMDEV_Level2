package com.dpdev.dao;

import com.dpdev.entity.OrderProduct;

import javax.persistence.EntityManager;

public class OrderProductRepository extends BaseRepository<Long, OrderProduct> {

    public OrderProductRepository(EntityManager entityManager) {
        super(OrderProduct.class, entityManager);
    }
}
