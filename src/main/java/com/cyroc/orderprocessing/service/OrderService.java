package com.cyroc.orderprocessing.service;

import org.springframework.stereotype.Service;

import com.cyroc.orderprocessing.model.Order;
import com.cyroc.orderprocessing.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
	}

	public Order parseOrder(String message) throws JsonProcessingException {
		// Deserialize JSON into Order object
		Order order = objectMapper.readValue(message, Order.class);

		// Manually validate 'status' field (since @NotNull is not validated during
		// deserialization)
		if (order.getStatus() == null) {
			throw new JsonProcessingException("Missing required field: 'status'") {

				private static final long serialVersionUID = -4907591686811093448L;
			};
		}

		return order;
	}

	public void processOrder(Order order) {
		if (!orderValidator.isValid(order)) {
			throw new IllegalArgumentException("Invalid order details");
		}

		try {
			updateStatus(order, "PROCESSING");
			simulateOrderProcessing(order);
			updateStatus(order, "COMPLETED");
		} catch (IllegalStateException | IllegalArgumentException e) {
			updateStatus(order, "FAILED");
			throw new RuntimeException("Order processing failed: " + order.getId(), e);
		}
	}

	public void processOrderById(String orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		processOrder(order);
	}

	protected void simulateOrderProcessing(Order order) {
		if (order.getQuantity() > 10) {
			throw new IllegalStateException("Not enough inventory for the product: " + order.getProduct());
		}

		if (!validatePayment(order)) {
			throw new IllegalStateException("Payment validation failed for order: " + order.getId());
		}
	}

	boolean validatePayment(Order order) {
		// need implementation
		return true;
	}

	public void updateStatus(Order order, String status) {
		order.setStatus(status);
		orderRepository.save(order);
	}
}
