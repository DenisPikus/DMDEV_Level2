package com.dpdev.dao;

import com.dpdev.entity.Orders;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Map;

@Repository
public class OrderRepository extends BaseRepository<Long, Orders> {

    public OrderRepository(EntityManager entityManager) {
        super(Orders.class, entityManager);
    }

    public Orders findByIdWithOrderedProducts(Long id) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("WithUserAndOrderedProducts")
        );
        return getEntityManager().find(Orders.class, id, properties);
    }
}
