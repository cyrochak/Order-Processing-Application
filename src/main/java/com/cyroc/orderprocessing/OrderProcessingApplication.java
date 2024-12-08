package com.cyroc.orderprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderProcessingApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderProcessingApplication.class, args);
		System.out.println("Real-Time Order Processing System is running...");
	}
}
