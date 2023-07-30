package com.dpdev.dao;

import com.dpdev.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.dpdev.entity.QOrderProduct.orderProduct;
import static com.dpdev.entity.QOrders.orders;
import static com.dpdev.entity.QUser.user;

@Repository
public class UserRepository extends BaseRepository<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<Object[]> findUsersWithAvgOrdersOrderedByEmail() {
        return getEntityManager().createQuery("select u.email, avg(op.quantity * op.price) from User u " +
                        "join u.orders o " +
                        "join o.orderProducts op " +
                        "group by u.email " +
                        "order by u.email", Object[].class)
                .getResultList();
    }

    public List<Tuple> findUsersWithAvgOrdersOrderedByEmailUsingQuerydsl() {
        return new JPAQuery<Tuple>(getEntityManager())
                .select(user.email, orderProduct.quantity.multiply(orderProduct.price).avg())
                .from(user)
                .join(user.orders, orders)
                .join(orders.orderProducts, orderProduct)
                .groupBy(user.email)
                .orderBy(user.email.asc())
                .fetch();
    }
}
