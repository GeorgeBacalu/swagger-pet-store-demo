package com.endava.petstore.unit.controller;

import com.endava.petstore.controller.UserController;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.UserMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private List<User> users;
    private List<User> newUsers;

    @BeforeEach
    void setUp() {
        user1 = getMockedUser1();
        user2 = getMockedUser2();
        user3 = getMockedUser3();
        user4 = getMockedUser4();
        users = getMockedUsers();
        newUsers = getMockedNewUsers();
    }

    @Test
    void findAll_test() {
        given(userService.findAll()).willReturn(users);
        ResponseEntity<List<User>> response = userController.findAll();
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(users);
    }

    @Test
    void findById_test() {
        given(userService.findById(VALID_ID)).willReturn(user1);
        ResponseEntity<User> response = userController.findById(VALID_ID);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user1);
    }

    @Test
    void save_test() {
        given(userService.save(any(User.class))).willReturn(user1);
        ResponseEntity<User> response = userController.save(user1);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody()).isEqualTo(user1);
    }

    @Test
    void update_test() {
        given(userService.update(any(User.class))).willReturn(user2);
        ResponseEntity<User> response = userController.update(user1);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user2);
    }

    @Test
    void deleteById_test() {
        ResponseEntity<Void> response = userController.deleteById(VALID_ID);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void saveAll_withArray_test() {
        User[] usersToSave = {user3, user4};
        given(userService.saveAll(usersToSave)).willReturn(newUsers);
        ResponseEntity<List<User>> response = userController.saveAll(usersToSave);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody()).isEqualTo(newUsers);
    }

    @Test
    void saveAll_withList_test() {
        List<User> usersToSave = List.of(user3, user4);
        given(userService.saveAll(usersToSave)).willReturn(newUsers);
        ResponseEntity<List<User>> response = userController.saveAll(usersToSave);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody()).isEqualTo(newUsers);
    }

    @Test
    void findByUsername_test() {
        given(userService.findByUsername(VALID_USERNAME)).willReturn(user1);
        ResponseEntity<User> response = userController.findByUsername(VALID_USERNAME);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user1);
    }

    @Test
    void updateByUsername_test() {
        given(userService.updateByUsername(any(User.class), any(String.class))).willReturn(user2);
        ResponseEntity<User> response = userController.updateByUsername(user1, VALID_USERNAME);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user2);
    }

    @Test
    void deleteByUsername_test() {
        ResponseEntity<Void> response = userController.deleteByUsername(VALID_USERNAME);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void login_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        given(userService.login("Username1", "#Password1")).willReturn(httpResponse);
        ResponseEntity<HttpResponse> response = userController.login("Username1", "#Password1");
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(httpResponse);
    }

    @Test
    void logout_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        given(userService.logout("Username2")).willReturn(httpResponse);
        ResponseEntity<HttpResponse> response = userController.logout("Username2");
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(httpResponse);
    }
}
