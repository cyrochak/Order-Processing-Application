package com.cyroc.orderprocessing.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cyroc.orderprocessing.model.Order;
import com.cyroc.orderprocessing.repository.OrderRepository;

@Service
public class OrderConsumer {
	private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
	private final OrderRepository orderRepository;

	public OrderConsumer(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@KafkaListener(topics = "order-topic", groupId = "order-group")
	public void consumeOrder(String orderId) {
		logger.info("Received order ID: {}", orderId);

		Order order = orderRepository.findById(orderId).orElse(null);

		if (order == null) {
			logger.error("Order with ID {} not found in the database.", orderId);
			return;
		}
		order.setStatus("COMPLETED");
		orderRepository.save(order);
		logger.info("Order ID {} has been processed successfully.", orderId);
	}
}