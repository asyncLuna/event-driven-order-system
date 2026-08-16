package dev.asyncluna.orders.order.service;

import dev.asyncluna.orders.order.entity.OrderStatus;

public record OrderStatusLookup(OrderStatus status, String source) {}
