package com.endava.petstore.unit.controller;

import com.endava.petstore.controller.StoreController;
import com.endava.petstore.model.Order;
import com.endava.petstore.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.VALID_ID;
import static com.endava.petstore.mock.StoreMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    @InjectMocks
    private StoreController storeController;

    @Mock
    private StoreService storeService;

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
        given(storeService.findAllOrders()).willReturn(orders);
        ResponseEntity<List<Order>> response = storeController.findAllOrders();
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(orders);
    }

    @Test
    void findOrderById_test() {
        given(storeService.findOrderById(VALID_ID)).willReturn(order1);
        ResponseEntity<Order> response = storeController.findOrderById(VALID_ID);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(order1);
    }

    @Test
    void saveOrder_test() {
        given(storeService.saveOrder(any(Order.class))).willReturn(order1);
        ResponseEntity<Order> response = storeController.saveOrder(order1);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody()).isEqualTo(order1);
    }

    @Test
    void updateOrder_test() {
        given(storeService.updateOrder(any(Order.class))).willReturn(order2);
        ResponseEntity<Order> response = storeController.updateOrder(order1);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(order2);
    }

    @Test
    void deleteOrderById_test() {
        ResponseEntity<Void> response = storeController.deleteOrderById(VALID_ID);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getInventoryByStatus_test() {
        given(storeService.getInventoryByStatus()).willReturn(inventory);
        ResponseEntity<Map<String, Integer>> response = storeController.getInventoryByStatus();
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(inventory);
    }
}
