package com.endava.petstore.controller;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/user", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Override @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<User> save(@RequestBody @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @Override @PutMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<User> update(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override @PostMapping(value = "/createWithArray", consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<List<User>> saveAll(@RequestBody @Valid User[] users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAll(users));
    }

    @Override @PostMapping(value = "/createWithList", consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<List<User>> saveAll(@RequestBody @Valid List<User> users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAll(users));
    }

    @Override @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @Override @PutMapping(value = "/username/{username}", consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<User> updateByUsername(@RequestBody @Valid User user, @PathVariable String username) {
        return ResponseEntity.ok(userService.updateByUsername(user, username));
    }

    @Override @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @Override @GetMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.login(username, password));
    }

    @Override @GetMapping("/logout")
    public ResponseEntity<HttpResponse> logout(@RequestParam String username) {
        return ResponseEntity.ok(userService.logout(username));
    }
}
