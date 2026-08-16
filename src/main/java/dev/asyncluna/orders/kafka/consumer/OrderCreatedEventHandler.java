package dev.asyncluna.orders.kafka.consumer;

import dev.asyncluna.orders.cache.OrderStatusCache;
import dev.asyncluna.orders.kafka.event.OrderCreatedEvent;
import dev.asyncluna.orders.kafka.producer.OrderEventProducer;
import dev.asyncluna.orders.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderCreatedEventHandler {
  private final OrderStatusCache statusCache;
  private final OrderEventProducer producer;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(OrderCreatedEvent event) {
    statusCache.put(event.orderId(), OrderStatus.PENDING);
    producer.publish(event);
  }
}
