package com.endava.petstore.integration.service;

import com.endava.petstore.model.Order;
import com.endava.petstore.repository.StoreRepository;
import com.endava.petstore.service.StoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.VALID_ID;
import static com.endava.petstore.mock.StoreMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StoreServiceIntegrationTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    private Order order1;
    private Order order2;
    private List<Order> orders;
    private Map<String, Integer> inventory;

    @BeforeEach
    void setUp() {
        order1 = getMockedOrder1();
        order2 = getMockedOrder2();
        orders = getMockedOrders();
        inventory = getMockedInventory();
    }

    @Test
    void findAllOrders_test() {
        given(storeRepository.findAllOrders()).willReturn(orders);
        List<Order> result = storeService.findAllOrders();
        then(result).isEqualTo(orders);
    }

    @Test
    void findOrderById_test() {
        given(storeRepository.findOrderById(VALID_ID)).willReturn(order1);
        Order result = storeService.findOrderById(VALID_ID);
        then(result).isEqualTo(order1);
    }

    @Test
    void saveOrder_test() {
        given(storeRepository.saveOrder(any(Order.class))).willReturn(order1);
        Order result = storeService.saveOrder(order1);
        verify(storeRepository).saveOrder(orderCaptor.capture());
        then(result).isEqualTo(orderCaptor.getValue());
    }

    @Test
    void updateOrder_test() {
        given(storeRepository.updateOrder(any(Order.class))).willReturn(order2);
        Order result = storeService.updateOrder(order1);
        then(result).isEqualTo(order2);
    }

    @Test
    void deleteOrderById_test() {
        storeService.deleteOrderById(VALID_ID);
        verify(storeRepository).deleteOrderById(VALID_ID);
    }

    @Test
    void getInventoryByStatus_test() {
        given(storeRepository.getInventoryByStatus()).willReturn(inventory);
        Map<String, Integer> result = storeService.getInventoryByStatus();
        then(result).isEqualTo(inventory);
    }
}
