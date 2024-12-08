package com.cyroc.orderprocessing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyroc.orderprocessing.kafka.OrderProducer;
import com.cyroc.orderprocessing.model.Order;
import com.cyroc.orderprocessing.repository.OrderRepository;
import com.cyroc.orderprocessing.service.OrderValidator;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderRepository orderRepository;
	private final OrderProducer orderProducer;
	private final OrderValidator orderValidator;

	public OrderController(OrderRepository orderRepository, OrderProducer orderProducer,
			OrderValidator orderValidator) {
		this.orderRepository = orderRepository;
		this.orderProducer = orderProducer;
		this.orderValidator = orderValidator;
	}

	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody Order order) {
		// Validate order before processing
		if (!orderValidator.isValid(order)) {
			return ResponseEntity.badRequest().body("Invalid order details");
		}

		// Set initial status and save
		order.setStatus("PENDING");
		Order savedOrder = orderRepository.save(order);

		// Publish to Kafka
		orderProducer.sendOrderEvent("order-topic", savedOrder.getId());
		return ResponseEntity.ok(savedOrder);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		return ResponseEntity.ok(orderRepository.findAll());
	}

}