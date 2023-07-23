package com.dpdev.dao;

import com.dpdev.entity.Stock;

import javax.persistence.EntityManager;

public class StockRepository extends BaseRepository<Long, Stock> {

    public StockRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Stock> getEntityClass() {
        return Stock.class;
    }
}
