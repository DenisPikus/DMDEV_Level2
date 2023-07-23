package com.dpdev.dao;

import com.dpdev.entity.Order;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityManager;
import java.util.Map;

public class OrderRepository extends BaseRepository<Long, Order> {

    public OrderRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }

    public Order findByIdWithOrderedProducts(Long id) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("WithUserAndOrderedProducts")
        );
        return getEntityManager().find(Order.class, id, properties);
    }
}
