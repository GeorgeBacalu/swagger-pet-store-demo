package com.endava.petstore.repository;

import com.endava.petstore.enums.OrderStatus;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.ORDER_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {
    private final PetRepository petRepository;
    private final Map<Long, Order> orders = new HashMap<>();

    @PostConstruct
    public void initializeOrders() {
        Order order1 = Order.builder()
              .id(1L)
              .petId(1L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
              .status(OrderStatus.PLACED)
              .complete(false)
              .build();
        orders.put(order1.getId(), order1);
        Order order2 = Order.builder()
              .id(2L)
              .petId(2L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 2, 0, 0, 0))
              .status(OrderStatus.APPROVED)
              .complete(false)
              .build();
        orders.put(order2.getId(), order2);
        Order order3 = Order.builder()
              .id(3L)
              .petId(3L)
              .quantity(2)
              .shipDate(LocalDateTime.of(2000, 1, 3, 0, 0, 0))
              .status(OrderStatus.DELIVERED)
              .complete(true)
              .build();
        orders.put(order3.getId(), order3);
    }

    @Override
    public List<Order> findAllOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order findOrderById(Long id) {
        return orders.values().stream().filter(order -> order.getId().equals(id)).findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, id)));
    }

    @Override
    public Order saveOrder(Order order) {
        petRepository.findById(order.getPetId());
        return orders.compute(order.getId(), (key, value) -> order);
    }

    @Override
    public Order updateOrder(Order order) {
        petRepository.findById(order.getPetId());
        Order orderToUpdate = findOrderById(order.getId());
        orderToUpdate.setPetId(order.getPetId());
        orderToUpdate.setQuantity(order.getQuantity());
        orderToUpdate.setShipDate(order.getShipDate());
        orderToUpdate.setStatus(order.getStatus());
        orderToUpdate.setComplete(order.getComplete());
        return orderToUpdate;
    }

    @Override
    public void deleteOrderById(Long id) {
        Order orderToDelete = findOrderById(id);
        orders.remove(orderToDelete.getId());
    }
}
