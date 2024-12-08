package com.cyroc.orderprocessing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cyroc.orderprocessing.model.Order;
import com.cyroc.orderprocessing.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderValidator orderValidator;

	private OrderService orderService;
	private Order validOrder;
	private Order invalidOrder;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		OrderServiceSubclass realService = new OrderServiceSubclass(orderRepository, orderValidator);
		orderService = spy(realService);
		validOrder = new Order("1", "Product 1", 5, 50.0, "PENDING");
		invalidOrder = new Order("2", "Product 2", 15, 150.0, "PENDING");
	}

	@Test
	void testProcessOrder_success() {
		when(orderValidator.isValid(validOrder)).thenReturn(true);
		when(orderRepository.save(any(Order.class))).thenReturn(validOrder);

		orderService.processOrder(validOrder);

		verify(orderRepository, times(2)).save(validOrder);
		assertEquals("COMPLETED", validOrder.getStatus());
	}

	@Test
	void testProcessOrder_invalidOrder() {
		when(orderValidator.isValid(invalidOrder)).thenReturn(false);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			orderService.processOrder(invalidOrder);
		});

		assertEquals("Invalid order details", exception.getMessage());
		verify(orderRepository, times(0)).save(invalidOrder);
	}

	@Test
	void testProcessOrder_paymentFailure() {
		when(orderValidator.isValid(validOrder)).thenReturn(true);
		when(orderRepository.save(any(Order.class))).thenReturn(validOrder);
		doThrow(new IllegalStateException("Payment validation failed")).when(orderService)
				.simulateOrderProcessing(validOrder);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.processOrder(validOrder);
		});

		assertEquals("Order processing failed: 1", exception.getMessage());
		assertEquals("FAILED", validOrder.getStatus());
		verify(orderRepository, times(2)).save(validOrder);
	}

	@Test
	void testParseOrder_success() throws JsonProcessingException {
		String json = "{\"id\":\"1\", \"product\":\"Product 1\", \"quantity\":5, \"price\":50.0, \"status\":\"PENDING\"}";
		Order expectedOrder = new Order("1", "Product 1", 5, 50.0, "PENDING");

		Order parsedOrder = orderService.parseOrder(json);

		assertEquals(expectedOrder.getId(), parsedOrder.getId());
		assertEquals(expectedOrder.getProduct(), parsedOrder.getProduct());
		assertEquals(expectedOrder.getQuantity(), parsedOrder.getQuantity());
		assertEquals(expectedOrder.getPrice(), parsedOrder.getPrice());
		assertEquals(expectedOrder.getStatus(), parsedOrder.getStatus());
	}

	@Test
	void testParseOrder_invalidJson() {
		String invalidJson = "{\"id\":\"1\", \"product\":\"Product 1\", \"quantity\":5, \"price\":50.0}";

		JsonProcessingException exception = assertThrows(JsonProcessingException.class, () -> {
			orderService.parseOrder(invalidJson);
		});

		assertTrue(exception.getMessage().contains("Missing required field: 'status'"));
	}

	@Test
	void testProcessOrderById_success() {
		when(orderRepository.findById("1")).thenReturn(java.util.Optional.of(validOrder));
		when(orderValidator.isValid(validOrder)).thenReturn(true);
		when(orderRepository.save(any(Order.class))).thenReturn(validOrder);

		orderService.processOrderById("1");

		verify(orderRepository, times(2)).save(validOrder);
		assertEquals("COMPLETED", validOrder.getStatus());
	}

	@Test
	void testProcessOrderById_orderNotFound() {
		when(orderRepository.findById("1")).thenReturn(java.util.Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.processOrderById("1");
		});

		assertEquals("Order not found", exception.getMessage());
	}

	// Subclassing OrderService to mock simulateOrderProcessing
	static class OrderServiceSubclass extends OrderService {
		public OrderServiceSubclass(OrderRepository orderRepository, OrderValidator orderValidator) {
			super(orderRepository, orderValidator);
		}

		@Override
		public void simulateOrderProcessing(Order order) {
		}
	}
}
