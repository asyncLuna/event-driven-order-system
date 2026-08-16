package dev.asyncluna.orders.order.dto;

import dev.asyncluna.orders.order.entity.Order;
import dev.asyncluna.orders.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
    UUID id, String customerId, OrderStatus status, BigDecimal totalPrice, Instant createdAt) {
  public static OrderResponse from(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getCustomerId(),
        order.getStatus(),
        order.getTotalPrice(),
        order.getCreatedAt());
  }
}
