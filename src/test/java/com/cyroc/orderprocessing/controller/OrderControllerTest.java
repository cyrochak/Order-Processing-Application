package com.cyroc.orderprocessing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.cyroc.orderprocessing.kafka.OrderProducer;
import com.cyroc.orderprocessing.model.Order;
import com.cyroc.orderprocessing.repository.OrderRepository;
import com.cyroc.orderprocessing.service.OrderValidator;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderProducer orderProducer;

	@Mock
	private OrderValidator orderValidator;

	@InjectMocks
	private OrderController orderController;

	@SuppressWarnings("deprecation")
	@Test
	void testCreateOrder_ValidOrder() {
		Order order = new Order("1", "productA", 2, 100.0, "PENDING");
		when(orderValidator.isValid(order)).thenReturn(true);
		when(orderRepository.save(order)).thenReturn(order);

		ResponseEntity<?> response = orderController.createOrder(order);

		assertEquals(200, response.getStatusCodeValue());
		verify(orderProducer, times(1)).sendOrderEvent("order-topic", order.getId());
	}

	@SuppressWarnings("deprecation")
	@Test
	void testCreateOrder_InvalidOrder() {
		Order order = new Order("1", "", 0, 0.0, "PENDING");
		when(orderValidator.isValid(order)).thenReturn(false);

		ResponseEntity<?> response = orderController.createOrder(order);

		assertEquals(400, response.getStatusCodeValue());
		verify(orderProducer, never()).sendOrderEvent(anyString(), anyString());
	}

	@Test
	void testGetAllOrders() {
		when(orderRepository.findAll())
				.thenReturn(Collections.singletonList(new Order("1", "productA", 2, 100.0, "PENDING")));

		ResponseEntity<List<Order>> response = orderController.getAllOrders();

		assertEquals(1, response.getBody().size());
		verify(orderRepository, times(1)).findAll();
	}
}
