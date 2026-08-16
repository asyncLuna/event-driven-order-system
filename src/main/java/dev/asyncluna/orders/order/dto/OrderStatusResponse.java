package dev.asyncluna.orders.order.dto;

import dev.asyncluna.orders.order.entity.OrderStatus;
import java.util.UUID;

public record OrderStatusResponse(UUID orderId, OrderStatus status, String source) {}
