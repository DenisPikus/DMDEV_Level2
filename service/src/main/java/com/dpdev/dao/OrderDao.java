package com.dpdev.dao;

import com.dpdev.entity.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class OrderDao {

    private static final OrderDao INSTANCE = new OrderDao();

    public Order findById(Session session, Long id) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.FETCH.getJpaHintName(), session.getEntityGraph("WithUserAndOrderedProducts")
        );
        return session.find(Order.class, id, properties);
    }

    public static OrderDao getInstance() {
        return INSTANCE;
    }
}
