package com.dpdev.repository;

import com.dpdev.entity.Orders;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @EntityGraph(attributePaths = {"user", "orderProducts.product"})
    public Orders findOrdersAndUsersById(Long id);
}
