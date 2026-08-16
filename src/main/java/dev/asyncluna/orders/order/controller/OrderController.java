package dev.asyncluna.orders.order.controller;

import dev.asyncluna.orders.order.dto.*;
import dev.asyncluna.orders.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
    return OrderResponse.from(service.create(request));
  }

  @GetMapping("/{id}")
  public OrderResponse get(@PathVariable UUID id) {
    return OrderResponse.from(service.get(id));
  }

  @GetMapping("/{id}/status")
  public OrderStatusResponse status(@PathVariable UUID id) {
    var lookup = service.getStatus(id);
    return new OrderStatusResponse(id, lookup.status(), lookup.source());
  }
}
