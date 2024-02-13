package com.endava.petstore.service;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);

    List<User> saveAll(User[] users);

    List<User> saveAll(List<User> users);

    User findByUsername(String username);

    User updateByUsername(User user, String username);

    void deleteByUsername(String username);

    HttpResponse login(String username, String password);

    HttpResponse logout(String username);
}
