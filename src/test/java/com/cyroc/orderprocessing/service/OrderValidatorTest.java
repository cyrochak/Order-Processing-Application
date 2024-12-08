package com.cyroc.orderprocessing.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.cyroc.orderprocessing.model.Order;

class OrderValidatorTest {

	private final OrderValidator orderValidator = new OrderValidator();

	@Test
	void testIsValid_ValidOrder() {
		Order order = new Order("1", "productA", 2, 100.0, "PENDING");
		assertTrue(orderValidator.isValid(order));
	}

	@Test
	void testIsValid_InvalidOrder() {
		Order order = new Order("1", "", 0, 0.0, "PENDING");
		assertFalse(orderValidator.isValid(order));
	}
}