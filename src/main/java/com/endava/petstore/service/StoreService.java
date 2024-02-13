package com.endava.petstore.service;

import com.endava.petstore.model.Order;

import java.util.List;
import java.util.Map;

public interface StoreService {

    List<Order> findAllOrders();

    Order findOrderById(Long id);

    Order saveOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrderById(Long id);

    Map<String, Integer> getInventoryByStatus();
}
