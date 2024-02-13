package com.endava.petstore.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.endava.petstore.constants.Constants.USERNAME_NOT_FOUND;
import static com.endava.petstore.constants.Constants.USER_NOT_FOUND;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @PostConstruct
    private void initializeUsers() {
        User user1 = User.builder()
              .id(1L)
              .username("Username1")
              .firstName("Firstname1")
              .lastName("Lastname1")
              .email("Email1@email.com")
              .password("#Password1")
              .phone("+40700 000 001")
              .status(1)
              .build();
        users.put(user1.getId(), user1);
        User user2 = User.builder()
              .id(2L)
              .username("Username2")
              .firstName("Firstname2")
              .lastName("Lastname2")
              .email("Email2@email.com")
              .password("#Password2")
              .phone("+40700 000 002")
              .status(2)
              .build();
        users.put(user2.getId(), user2);
        User user3 = User.builder()
              .id(3L)
              .username("Username3")
              .firstName("Firstname3")
              .lastName("Lastname3")
              .email("Email3@email.com")
              .password("#Password3")
              .phone("+40700 000 003")
              .status(3)
              .build();
        users.put(user3.getId(), user3);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {
        return findAll().stream().filter(user -> user.getId().equals(id)).findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    @Override
    public User save(User user) {
        return users.compute(user.getId(), (key, value) -> user);
    }

    @Override
    public User update(User user) {
        User userToUpdate = findById(user.getId());
        return getUpdatedUser(user, userToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        User userToDelete = findById(id);
        users.remove(userToDelete.getId());
    }

    @Override
    public List<User> saveAll(User[] users) {
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(save(user));
        }
        return userList;
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return users.stream().map(this::save).toList();
    }

    @Override
    public User findByUsername(String username) {
        return findAll().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND, username)));
    }

    @Override
    public User updateByUsername(User user, String username) {
        User userToUpdate = findByUsername(username);
        return getUpdatedUser(user, userToUpdate);
    }

    @Override
    public void deleteByUsername(String username) {
        User userToDelete = findByUsername(username);
        users.remove(userToDelete.getId());
    }

    private User getUpdatedUser(User user, User userToUpdate) {
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setStatus(user.getStatus());
        return userToUpdate;
    }
}
