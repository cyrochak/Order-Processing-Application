package com.cyroc.orderprocessing.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cyroc.orderprocessing.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}
