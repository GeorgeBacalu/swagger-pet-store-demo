package com.endava.petstore.unit.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Order;
import com.endava.petstore.repository.StoreRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.StoreMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreRepositoryImplTest {

    @Mock
    private StoreRepositoryImpl storeRepository;

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
        List<Order> result = storeRepository.findAllOrders();
        then(result).isEqualTo(orders);
    }

    @Test
    void findOrderById_validId_test() {
        given(storeRepository.findOrderById(VALID_ID)).willReturn(order1);
        Order result = storeRepository.findOrderById(VALID_ID);
        then(result).isEqualTo(order1);
    }

    @Test
    void findOrderById_invalidId_test() {
        given(storeRepository.findOrderById(INVALID_ID)).willThrow(new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, INVALID_ID)));
        thenThrownBy(() -> storeRepository.findOrderById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(ORDER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void saveOrder_test() {
        given(storeRepository.saveOrder(any(Order.class))).willReturn(order1);
        Order result = storeRepository.saveOrder(order1);
        verify(storeRepository).saveOrder(orderCaptor.capture());
        then(result).isEqualTo(orderCaptor.getValue());
    }

    @Test
    void updateOrder_forValidPet_test() {
        given(storeRepository.updateOrder(any(Order.class))).willReturn(order2);
        Order result = storeRepository.updateOrder(order1);
        then(result).isEqualTo(order2);
    }

    @Test
    void deleteOrderById_validId_test() {
        storeRepository.deleteOrderById(VALID_ID);
        verify(storeRepository).deleteOrderById(VALID_ID);
    }

    @Test
    void deleteOrderById_invalidId_test() {
        doThrow(new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, INVALID_ID))).when(storeRepository).deleteOrderById(INVALID_ID);
        thenThrownBy(() -> storeRepository.deleteOrderById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(ORDER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void getInventoryByStatus_test() {
        given(storeRepository.getInventoryByStatus()).willReturn(inventory);
        Map<String, Integer> result = storeRepository.getInventoryByStatus();
        then(result).isEqualTo(inventory);
    }
}
