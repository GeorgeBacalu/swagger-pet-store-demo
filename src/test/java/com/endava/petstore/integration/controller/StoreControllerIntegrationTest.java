package com.endava.petstore.integration.controller;

import com.endava.petstore.model.Order;
import com.endava.petstore.repository.StoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.StoreMock.*;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreControllerIntegrationTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
    void findAllOrders_test() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_STORE, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Order> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(orders);
    }

    @Test
    void findOrderById_validId_test() {
        ResponseEntity<Order> response = testRestTemplate.getForEntity(API_STORE + "/" + VALID_ID, Order.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(order1);
    }

    @Test
    void findOrderById_invalidId_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_STORE + "/" + INVALID_ID, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(ORDER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void saveOrder_test() throws Exception {
        ResponseEntity<Order> saveResponse = testRestTemplate.postForEntity(API_STORE, order4, Order.class);
        then(saveResponse).isNotNull();
        then(saveResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(saveResponse.getBody()).isEqualTo(order4);
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_STORE, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Order> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(order1, order2, order3, order4));
    }

    @Test
    void updateOrder_test() {
        Order updatedOrder = order1;
        updatedOrder.setQuantity(3);
        ResponseEntity<Order> updateResponse = testRestTemplate.exchange(API_STORE, HttpMethod.PUT, new HttpEntity<>(order1), Order.class);
        then(updateResponse).isNotNull();
        then(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(updateResponse.getBody()).isEqualTo(updatedOrder);
        ResponseEntity<Order> getResponse = testRestTemplate.getForEntity(API_STORE + "/" + VALID_ID, Order.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(getResponse.getBody()).isEqualTo(updatedOrder);
    }

    @Test
    void deleteOrderById_validId_test() throws Exception {
        ResponseEntity<Order> deleteResponse = testRestTemplate.exchange(API_STORE + "/" + VALID_ID, HttpMethod.DELETE, null, Order.class);
        then(deleteResponse).isNotNull();
        then(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<String> getResponse = testRestTemplate.getForEntity(API_STORE + "/" + VALID_ID, String.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(getResponse.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(ORDER_NOT_FOUND, VALID_ID));
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_STORE, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Order> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(order2, order3));
    }

    @Test
    void deleteOrderById_invalidId_test() {
        ResponseEntity<String> response = testRestTemplate.exchange(API_STORE + "/" + INVALID_ID, HttpMethod.DELETE, null, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(ORDER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void getInventoryByStatus_test() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_STORE + "/inventory", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Integer> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(inventory);
    }
}
