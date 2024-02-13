package com.endava.petstore.integration.controller;

import com.endava.petstore.controller.StoreController;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Order;
import com.endava.petstore.service.StoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.StoreMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
@ExtendWith(MockitoExtension.class)
class StoreControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void findAllOrders_test() throws Exception {
        given(storeService.findAllOrders()).willReturn(orders);
        ResultActions actions = mockMvc.perform(get(API_STORE)).andExpect(status().isOk());
        for (int i = 0; i < orders.size(); ++i) {
            assertOrder(actions, "$[" + i + "]", orders.get(i));
        }
        List<Order> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {});
        then(result).isEqualTo(orders);
    }

    @Test
    void findOrderById_validId_test() throws Exception {
        given(storeService.findOrderById(VALID_ID)).willReturn(order1);
        ResultActions actions = mockMvc.perform(get(API_STORE + "/{id}", VALID_ID)).andExpect(status().isOk());
        assertOrderJson(actions, order1);
        Order result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Order.class);
        then(result).isEqualTo(order1);
    }

    @Test
    void findOrderById_invalidId_test() throws Exception {
        String message = String.format(ORDER_NOT_FOUND, INVALID_ID);
        given(storeService.findOrderById(INVALID_ID)).willThrow(new ResourceNotFoundException(message));
        mockMvc.perform(get(API_STORE + "/{id}", INVALID_ID))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void saveOrder_test() throws Exception {
        given(storeService.saveOrder(any(Order.class))).willReturn(order1);
        ResultActions actions = mockMvc.perform(post(API_STORE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(order1)))
              .andExpect(status().isCreated());
        assertOrderJson(actions, order1);
        Order result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Order.class);
        then(result).isEqualTo(order1);
    }

    @Test
    void updateOrder_test() throws Exception {
        given(storeService.updateOrder(any(Order.class))).willReturn(order2);
        ResultActions actions = mockMvc.perform(put(API_STORE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(order2)))
              .andExpect(status().isOk());
        assertOrderJson(actions, order2);
        Order result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Order.class);
        then(result).isEqualTo(order2);
    }

    @Test
    void deleteOrderById_validId_test() throws Exception {
        mockMvc.perform(delete(API_STORE + "/{id}", VALID_ID)).andExpect(status().isNoContent()).andReturn();
        verify(storeService).deleteOrderById(VALID_ID);
    }

    @Test
    void deleteOrderById_invalidId_test() throws Exception {
        String message = String.format(ORDER_NOT_FOUND, INVALID_ID);
        doThrow(new ResourceNotFoundException(message)).when(storeService).deleteOrderById(INVALID_ID);
        mockMvc.perform(delete(API_STORE + "/{id}", INVALID_ID))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void getInventoryByStatus_test() throws Exception {
        given(storeService.getInventoryByStatus()).willReturn(inventory);
        ResultActions actions = mockMvc.perform(get(API_STORE + "/inventory")).andExpect(status().isOk());
        actions.andExpect(jsonPath("$.AVAILABLE").value(1))
              .andExpect(jsonPath("$.PENDING").value(1))
              .andExpect(jsonPath("$.SOLD").value(1));
        Map<String, Integer> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {});
        then(result).isEqualTo(inventory);
    }

    private void assertOrder(ResultActions actions, String prefix, Order order) throws Exception {
        actions.andExpect(jsonPath(prefix + ".id").value(order.getId()))
              .andExpect(jsonPath(prefix + ".petId").value(order.getPetId()))
              .andExpect(jsonPath(prefix + ".quantity").value(order.getQuantity()))
              .andExpect(jsonPath(prefix + ".shipDate").value(order.getShipDate() + ":00"))
              .andExpect(jsonPath(prefix + ".status").value(order.getStatus().name()))
              .andExpect(jsonPath(prefix + ".complete").value(order.getComplete()));
    }

    private void assertOrderJson(ResultActions actions, Order order) throws Exception {
        actions.andExpect(jsonPath("$.id").value(order.getId()))
              .andExpect(jsonPath("$.petId").value(order.getPetId()))
              .andExpect(jsonPath("$.quantity").value(order.getQuantity()))
              .andExpect(jsonPath("$.shipDate").value(order.getShipDate() + ":00"))
              .andExpect(jsonPath("$.status").value(order.getStatus().name()))
              .andExpect(jsonPath("$.complete").value(order.getComplete()));
    }
}
