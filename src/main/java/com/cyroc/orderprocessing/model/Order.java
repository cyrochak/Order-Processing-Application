package com.cyroc.orderprocessing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {
	@Id
	private String id;
	private String product;
	private int quantity;
	private double price;
	@NotNull(message = "Status cannot be null")
	private String status; // PENDING, PROCESSED, FAILED
}
