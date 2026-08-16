package dev.asyncluna.orders.kafka.consumer;

import dev.asyncluna.orders.kafka.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {
  @KafkaListener(topics = "${app.kafka.order-created-topic}", groupId = "notification-service")
  public void handle(OrderCreatedEvent event) {
    log.info(
        "Order confirmation notification queued: orderId={}, customerId={}",
        event.orderId(),
        event.customerId());
  }
}
