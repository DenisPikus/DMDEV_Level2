package com.dpdev.dao;

import com.dpdev.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import static com.dpdev.entity.QOrder.order;
import static com.dpdev.entity.QOrderProduct.orderProduct;
import static com.dpdev.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    public List<User> findAll(Session session) {
        return session.createQuery("select u from User  u", User.class)
                .list();
    }

    public Optional<User> findById(Session session, Long id) {
        return session.createQuery("select u from User u " +
                        "where u.id = :id", User.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    public List<Object[]> findUsersWithAvgOrdersOrderedByEmail(Session session) {
        return session.createQuery("select u.email, avg(op.quantity * op.price) from User u " +
                        "join u.orders o " +
                        "join o.orderProducts op " +
                        "group by u.email " +
                        "order by u.email", Object[].class)
                .list();
    }

    public List<Tuple> findUsersWithAvgOrdersOrderedByEmailUsingQuerydsl(Session session) {
        return new JPAQuery<Tuple>(session)
                .select(user.email, orderProduct.quantity.multiply(orderProduct.price).avg())
                .from(user)
                .join(user.orders, order)
                .join(order.orderProducts, orderProduct)
                .groupBy(user.email)
                .orderBy(user.email.asc())
                .fetch();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
