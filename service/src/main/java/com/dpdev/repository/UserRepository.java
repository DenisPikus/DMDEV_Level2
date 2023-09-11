package com.dpdev.repository;

import com.dpdev.dto.UserWithAvgPriceProjection;
import com.dpdev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository, QuerydslPredicateExecutor<User> {

    @Query(value = "select u.username as email, avg(op.quantity * op.price) as avgPrice from User u " +
            "join u.orders o " +
            "join o.orderItems op " +
            "group by u.username " +
            "order by u.username")
    List<UserWithAvgPriceProjection> findUsersWithAvgOrdersOrderedByEmail();

    Optional<User> findUserByUsername(String username);

}
