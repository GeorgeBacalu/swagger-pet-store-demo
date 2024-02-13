package com.endava.petstore.controller;

import com.endava.petstore.model.Order;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Api(value = "Store REST Controller", description = "Access to Petstore orders", tags = "store")
public interface StoreApi {

    @ApiOperation(value = "Get all orders", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<List<Order>> findAllOrders();

    @ApiOperation(value = "Find purchase order by ID", notes = "For valid response try integer IDs with values between 1 and 3. Other values will generate exceptions", response = Order.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Order not found")})
    ResponseEntity<Order> findOrderById(@ApiParam(value = "ID of order that needs to be fetched", example = "1", required = true) Long id);

    @ApiOperation(value = "Place an order for a pet", response = Order.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid order")})
    ResponseEntity<Order> saveOrder(@ApiParam(value = "Order placed for purchasing the pet", required = true) Order order);

    @ApiOperation(value = "Update an existing order", response = Order.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Order not found"),
          @ApiResponse(code = 405, message = "Validation exception")})
    ResponseEntity<Order> updateOrder(@ApiParam(value = "Updated order for purchasing the pet", required = true) Order order);

    @ApiOperation(value = "Delete purchase order by ID", notes = "For valid response try integer IDs with positive integer values. Negative or non-integer values will generate API errors")
    @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Order not found")})
    ResponseEntity<Void> deleteOrderById(@ApiParam(value = "ID of order that needs to be deleted", example = "1", required = true) Long id);

    @ApiOperation(value = "Returns pet inventories by status", notes = "Returns a map of status codes to quantities", response = Map.class)
    @ApiResponse(code = 200, message = "Successful operation")
    ResponseEntity<Map<String, Integer>> getInventoryByStatus();
}
