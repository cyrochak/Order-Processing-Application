package com.cyroc.orderprocessing.service;

import org.springframework.stereotype.Component;

import com.cyroc.orderprocessing.model.Order;

@Component
public class OrderValidator {

	public boolean isValid(Order order) {
		if (order.getProduct() == null || order.getProduct().isEmpty()) {
			return false;
		}
		if (order.getQuantity() <= 0) {
			return false;
		}
		if (order.getPrice() <= 0) {
			return false;
		}
		return true;
	}
}