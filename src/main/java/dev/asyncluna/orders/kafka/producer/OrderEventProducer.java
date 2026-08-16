package dev.asyncluna.orders.kafka.producer;

import dev.asyncluna.orders.kafka.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {
  private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

  @Value("${app.kafka.order-created-topic}")
  private String topic;

  public void publish(OrderCreatedEvent event) {
    kafkaTemplate.send(topic, event.orderId().toString(), event);
  }
}
