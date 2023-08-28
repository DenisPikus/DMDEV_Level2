package com.dpdev.repository;

import com.dpdev.dto.UserWithAvgPriceProjection;
import com.dpdev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository, QuerydslPredicateExecutor<User> {

    @Query(value = "select u.email as email, avg(op.quantity * op.price) as avgPrice from User u " +
            "join u.orders o " +
            "join o.orderItems op " +
            "group by u.email " +
            "order by u.email")
    List<UserWithAvgPriceProjection> findUsersWithAvgOrdersOrderedByEmail();
}
