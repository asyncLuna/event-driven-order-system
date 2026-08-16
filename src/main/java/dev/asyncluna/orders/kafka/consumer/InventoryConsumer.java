package dev.asyncluna.orders.kafka.consumer;

import dev.asyncluna.orders.cache.OrderStatusCache;
import dev.asyncluna.orders.kafka.event.OrderCreatedEvent;
import dev.asyncluna.orders.order.entity.OrderStatus;
import dev.asyncluna.orders.order.repository.OrderRepository;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InventoryConsumer {
  private final OrderStatusCache cache;
  private final OrderRepository orders;

  @KafkaListener(topics = "${app.kafka.order-created-topic}", groupId = "inventory-service")
  @Transactional
  public void handle(OrderCreatedEvent event) {
    if (!cache.markProcessed(event.eventId())) return;
    boolean available = ThreadLocalRandom.current().nextInt(10) != 0;
    OrderStatus status = available ? OrderStatus.CONFIRMED : OrderStatus.CANCELLED;
    orders.findById(event.orderId()).ifPresent(order -> order.setStatus(status));
    cache.put(event.orderId(), status);
  }
}
