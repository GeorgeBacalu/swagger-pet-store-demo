package com.endava.petstore.service;

import com.endava.petstore.model.Order;
import com.endava.petstore.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public List<Order> findAllOrders() {
        return storeRepository.findAllOrders();
    }

    @Override
    public Order findOrderById(Long id) {
        return storeRepository.findOrderById(id);
    }

    @Override
    public Order saveOrder(Order order) {
        return storeRepository.saveOrder(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return storeRepository.updateOrder(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        storeRepository.deleteOrderById(id);
    }

    @Override
    public Map<String, Integer> getInventoryByStatus() {
        return storeRepository.getInventoryByStatus();
    }
}
