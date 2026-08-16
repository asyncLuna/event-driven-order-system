package dev.asyncluna.orders.order.service;

import dev.asyncluna.orders.cache.OrderStatusCache;
import dev.asyncluna.orders.kafka.event.*;
import dev.asyncluna.orders.order.dto.CreateOrderRequest;
import dev.asyncluna.orders.order.entity.*;
import dev.asyncluna.orders.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository repository;
  private final OrderStatusCache statusCache;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public Order create(CreateOrderRequest request) {
    UUID id = UUID.randomUUID();
    BigDecimal total =
        request.items().stream()
            .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    Order order = new Order(id, request.customerId(), total, Instant.now());
    request
        .items()
        .forEach(
            item ->
                order.addItem(new OrderItem(item.productId(), item.quantity(), item.unitPrice())));
    Order saved = repository.save(order);
    eventPublisher.publishEvent(
        new OrderCreatedEvent(
            UUID.randomUUID(),
            id,
            request.customerId(),
            request.items().stream()
                .map(item -> new OrderItemEvent(item.productId(), item.quantity()))
                .toList(),
            saved.getCreatedAt()));
    return saved;
  }

  @Transactional(readOnly = true)
  public Order get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
  }

  @Transactional(readOnly = true)
  public OrderStatusLookup getStatus(UUID id) {
    return statusCache
        .get(id)
        .map(status -> new OrderStatusLookup(status, "redis"))
        .orElseGet(
            () -> {
              OrderStatus status = get(id).getStatus();
              statusCache.put(id, status);
              return new OrderStatusLookup(status, "postgresql");
            });
  }
}
