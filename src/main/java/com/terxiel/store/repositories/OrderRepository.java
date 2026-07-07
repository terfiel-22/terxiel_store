package com.terxiel.store.repositories;

import com.terxiel.store.entities.Order;
import com.terxiel.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems.product")
    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> getOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "orderItems.product")
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> getOrderWithItems(@Param("id") Long id);
}
