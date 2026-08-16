package dev.asyncluna.orders.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
    @NotBlank @Size(max = 100) String customerId, @NotEmpty @Valid List<Item> items) {
  public record Item(
      @NotBlank @Size(max = 100) String productId,
      @Positive int quantity,
      @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal unitPrice) {}
}
