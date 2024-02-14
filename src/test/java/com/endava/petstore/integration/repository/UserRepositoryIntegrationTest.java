package com.endava.petstore.integration.repository;

import com.endava.petstore.exception.InvalidRequestException;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.UserMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@SpringBootTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepositoryImpl userRepository;

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
        userRepository.deleteAll();
        users.forEach(user -> userRepository.save(user));
    }

    @Test
    void findAll_test() {
        then(userRepository.findAll()).isEqualTo(users);
    }

    @Test
    void findById_validId_test() {
        then(userRepository.findById(VALID_ID)).isEqualTo(user1);
    }

    @Test
    void findById_invalidId_test() {
        thenThrownBy(() -> userRepository.findById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void save_test() {
        then(userRepository.save(user3)).isEqualTo(user3);
        then(userRepository.findAll()).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    void update_test() {
        then(userRepository.update(user1)).isEqualTo(user1);
    }

    @Test
    void deleteById_validId_test() {
        userRepository.deleteById(VALID_ID);
        then(userRepository.findAll()).isEqualTo(List.of(user2));
    }

    @Test
    void deleteById_invalidId_test() {
        thenThrownBy(() -> userRepository.deleteById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void saveAll_withArray_test() {
        User[] usersToSave = {user3, user4};
        then(userRepository.saveAll(usersToSave)).isEqualTo(List.of(user3, user4));
        then(userRepository.findAll()).isEqualTo(newUsers);
    }

    @Test
    void saveAll_withList_test() {
        List<User> usersToSave = List.of(user3, user4);
        then(userRepository.saveAll(usersToSave)).isEqualTo(List.of(user3, user4));
        then(userRepository.findAll()).isEqualTo(newUsers);
    }

    @Test
    void findByUsername_validUsername_test() {
        then(userRepository.findByUsername(VALID_USERNAME)).isEqualTo(user1);
    }

    @Test
    void findByUsername_invalidUsername_test() {
        thenThrownBy(() -> userRepository.findByUsername(INVALID_USERNAME))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USERNAME_NOT_FOUND, INVALID_USERNAME));
    }

    @Test
    void updateByUsername_test() {
        then(userRepository.updateByUsername(user1, VALID_USERNAME)).isEqualTo(user1);
    }

    @Test
    void deleteByUsername_validUsername_test() {
        userRepository.deleteByUsername(VALID_USERNAME);
        then(userRepository.findAll()).isEqualTo(List.of(user2));
    }

    @Test
    void deleteByUsername_invalidUsername_test() {
        thenThrownBy(() -> userRepository.deleteByUsername(INVALID_USERNAME))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USERNAME_NOT_FOUND, INVALID_USERNAME));
    }

    @Test
    void login_success_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        HttpResponse result = userRepository.login("Username1", "#Password1");
        then(result.getCode()).isEqualTo(httpResponse.getCode());
        then(result.getType()).isEqualTo(httpResponse.getType());
        then(getTimestamp(result.getMessage()) - getTimestamp(httpResponse.getMessage())).isLessThan(MAX_ELAPSED_TIME);
    }

    @Test
    void login_userAlreadyLoggedIn_test() {
        thenThrownBy(() -> userRepository.login("Username2", "#Password2"))
              .isInstanceOf(InvalidRequestException.class)
              .hasMessage(String.format(USER_ALREADY_LOGGED_IN, "Username2"));
    }

    @Test
    void login_userNotFound_test() {
        thenThrownBy(() -> userRepository.login("Username1", "#Password2"))
              .isInstanceOf(InvalidRequestException.class)
              .hasMessage(String.format(INVALID_USER, "Username1", "#Password2"));
    }

    @Test
    void logout_success_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_OUT, System.nanoTime()));
        HttpResponse result = userRepository.logout("Username2");
        then(result.getCode()).isEqualTo(httpResponse.getCode());
        then(result.getType()).isEqualTo(httpResponse.getType());
        then(getTimestamp(result.getMessage()) - getTimestamp(httpResponse.getMessage())).isLessThan(MAX_ELAPSED_TIME);
    }

    @Test
    void logout_userAlreadyLoggedOut_test() {
        thenThrownBy(() -> userRepository.logout("Username1"))
              .isInstanceOf(InvalidRequestException.class)
              .hasMessage(String.format(USER_ALREADY_LOGGED_OUT, "Username1"));
    }

    private long getTimestamp(String message) {
        return Long.parseLong(message.substring(message.lastIndexOf(" ") + 1));
    }
}
