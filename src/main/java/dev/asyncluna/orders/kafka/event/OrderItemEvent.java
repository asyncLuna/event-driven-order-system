package dev.asyncluna.orders.kafka.event;

public record OrderItemEvent(String productId, int quantity) {}
