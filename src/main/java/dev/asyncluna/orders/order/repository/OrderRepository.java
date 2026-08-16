package dev.asyncluna.orders.order.repository;

import dev.asyncluna.orders.order.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {}
