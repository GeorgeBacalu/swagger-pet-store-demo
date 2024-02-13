package com.endava.petstore.controller;

import com.endava.petstore.model.Order;
import com.endava.petstore.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/store", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class StoreController implements StoreApi {
    private final StoreService storeService;

    @Override @GetMapping
    public ResponseEntity<List<Order>> findAllOrders() {
        return ResponseEntity.ok(storeService.findAllOrders());
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.findOrderById(id));
    }

    @Override @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Order> saveOrder(@RequestBody @Valid Order order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.saveOrder(order));
    }

    @Override @PutMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Order> updateOrder(@RequestBody @Valid Order order) {
        return ResponseEntity.ok(storeService.updateOrder(order));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        storeService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
