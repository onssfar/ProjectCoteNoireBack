package com.cotenoire.controller;

import com.cotenoire.dto.*;
import com.cotenoire.enums.OrderStatus;
import com.cotenoire.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(r));
    }

    @GetMapping("/{id}")
    public OrderResponse one(@PathVariable Long id) {
        return orderService.find(id);
    }
    @GetMapping
    public List<OrderResponse> all() {
        return orderService.findAll();
    }

    @GetMapping("/number/{number}")
    public OrderResponse number(@PathVariable String number) {
        return orderService.findNumber(number);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse status(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.status(id, status);
    }
}
