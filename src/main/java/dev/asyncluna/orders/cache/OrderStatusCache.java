package dev.asyncluna.orders.cache;

import dev.asyncluna.orders.order.entity.OrderStatus;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusCache {
  private final StringRedisTemplate redis;

  @Value("${app.cache.status-ttl}")
  private Duration statusTtl;

  @Value("${app.cache.idempotency-ttl}")
  private Duration idempotencyTtl;

  public void put(UUID orderId, OrderStatus status) {
    redis.opsForValue().set(key(orderId), status.name(), statusTtl);
  }

  public Optional<OrderStatus> get(UUID orderId) {
    String value = redis.opsForValue().get(key(orderId));
    if (value == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(OrderStatus.valueOf(value));
    } catch (IllegalArgumentException exception) {
      redis.delete(key(orderId));
      return Optional.empty();
    }
  }

  public boolean markProcessed(UUID eventId) {
    return Boolean.TRUE.equals(
        redis.opsForValue().setIfAbsent("processed:" + eventId, "true", idempotencyTtl));
  }

  private String key(UUID orderId) {
    return "order:" + orderId + ":status";
  }
}
