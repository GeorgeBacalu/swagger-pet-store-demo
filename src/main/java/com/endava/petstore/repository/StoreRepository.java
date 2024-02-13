package com.endava.petstore.repository;

import com.endava.petstore.model.Order;

import java.util.List;
import java.util.Map;

public interface StoreRepository {

    List<Order> findAllOrders();

    Order findOrderById(Long id);

    Order saveOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrderById(Long id);

    Map<String, Integer> getInventoryByStatus();

    void deleteAllOrders();
}
