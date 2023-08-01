package com.dpdev.dao;

import com.dpdev.entity.Stock;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class StockRepository extends BaseRepository<Long, Stock> {

    public StockRepository(EntityManager entityManager) {
        super(Stock.class, entityManager);
    }
}
