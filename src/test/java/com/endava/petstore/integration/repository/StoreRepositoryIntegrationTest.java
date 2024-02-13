package com.endava.petstore.integration.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Order;
import com.endava.petstore.repository.StoreRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.StoreMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@SpringBootTest
class StoreRepositoryIntegrationTest {

    @Autowired
    private StoreRepositoryImpl storeRepository;

    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;
    private List<Order> orders;
    private Map<String, Integer> inventory;

    @BeforeEach
    void setUp() {
        order1 = getMockedOrder1();
        order2 = getMockedOrder2();
        order3 = getMockedOrder3();
        order4 = getMockedOrder4();
        orders = getMockedOrders();
        inventory = getMockedInventory();
        storeRepository.deleteAllOrders();
        orders.forEach(order -> storeRepository.saveOrder(order));
    }

    @Test
    void findAllOrders_test() {
        then(storeRepository.findAllOrders()).isEqualTo(orders);
    }

    @Test
    void findOrderById_validId_test() {
        then(storeRepository.findOrderById(VALID_ID)).isEqualTo(order1);
    }

    @Test
    void findOrderById_invalidId_test() {
        thenThrownBy(() -> storeRepository.findOrderById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(ORDER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void saveOrder_test() {
        then(storeRepository.saveOrder(order4)).isEqualTo(order4);
        then(storeRepository.findAllOrders()).isEqualTo(List.of(order1, order2, order3, order4));
    }

    @Test
    void updateOrder_test() {
        then(storeRepository.updateOrder(order1)).isEqualTo(order1);
    }

    @Test
    void deleteOrderById_validId_test() {
        storeRepository.deleteOrderById(VALID_ID);
        then(storeRepository.findAllOrders()).isEqualTo(List.of(order2, order3));
    }

    @Test
    void deleteOrderById_invalidId_test() {
        thenThrownBy(() -> storeRepository.deleteOrderById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(ORDER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void getInventoryByStatus_test() {
        then(storeRepository.getInventoryByStatus()).isEqualTo(inventory);
    }
}
