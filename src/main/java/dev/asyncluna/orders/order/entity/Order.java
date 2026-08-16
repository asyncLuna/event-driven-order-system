package dev.asyncluna.orders.order.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
  @Id private UUID id;

  @Column(name = "customer_id", nullable = false, length = 100)
  private String customerId;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private OrderStatus status;

  @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalPrice;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Getter(AccessLevel.NONE)
  private final List<OrderItem> items = new ArrayList<>();

  public Order(UUID id, String customerId, BigDecimal totalPrice, Instant createdAt) {
    this.id = id;
    this.customerId = customerId;
    this.totalPrice = totalPrice;
    this.createdAt = createdAt;
    this.status = OrderStatus.PENDING;
  }

  public void addItem(OrderItem item) {
    items.add(item);
    item.attachTo(this);
  }

  public List<OrderItem> getItems() {
    return List.copyOf(items);
  }
}
