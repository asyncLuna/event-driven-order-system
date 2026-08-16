package dev.asyncluna.orders.kafka.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
    UUID eventId, UUID orderId, String customerId, List<OrderItemEvent> items, Instant createdAt) {}
