package com.dpdev.repository;

import com.dpdev.dto.UserWithAvgPriceProjection;
import com.dpdev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u.email, avg(op.quantity * op.price) as avgPrice from User u " +
            "join u.orders o " +
            "join o.orderProducts op " +
            "group by u.email " +
            "order by u.email")
    public List<UserWithAvgPriceProjection> findUsersWithAvgOrdersOrderedByEmail();
}
