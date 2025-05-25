package org.example.firstspringapp.repository;

import org.example.firstspringapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<Order, Long> {
}