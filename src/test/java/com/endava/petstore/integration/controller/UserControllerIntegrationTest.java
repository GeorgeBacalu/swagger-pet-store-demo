package com.endava.petstore.integration.controller;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.repository.UserRepository;
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
import java.util.Objects;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.UserMock.*;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
    void findAll_test() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(users);
    }

    @Test
    void findById_validId_test() {
        ResponseEntity<User> response = testRestTemplate.getForEntity(API_USERS + "/" + VALID_ID, User.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user1);
    }

    @Test
    void findById_invalidId_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS + "/" + INVALID_ID, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(USER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void save_test() throws Exception {
        ResponseEntity<User> saveResponse = testRestTemplate.postForEntity(API_USERS, user3, User.class);
        then(saveResponse).isNotNull();
        then(saveResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(saveResponse.getBody()).isEqualTo(user3);
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_USERS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    void update_test() {
        User updatedUser = user1;
        updatedUser.setUsername("New Username");
        ResponseEntity<User> updateResponse = testRestTemplate.exchange(API_USERS, HttpMethod.PUT, new HttpEntity<>(user1), User.class);
        then(updateResponse).isNotNull();
        then(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(updateResponse.getBody()).isEqualTo(updatedUser);
        ResponseEntity<User> getResponse = testRestTemplate.getForEntity(API_USERS + "/" + VALID_ID, User.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(getResponse.getBody()).isEqualTo(updatedUser);
    }

    @Test
    void deleteById_validId_test() throws Exception {
        ResponseEntity<User> deleteResponse = testRestTemplate.exchange(API_USERS + "/" + VALID_ID, HttpMethod.DELETE, null, User.class);
        then(deleteResponse).isNotNull();
        then(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<String> getResponse = testRestTemplate.getForEntity(API_USERS + "/" + VALID_ID, String.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(getResponse.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(USER_NOT_FOUND, VALID_ID));
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_USERS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(user2));
    }

    @Test
    void deleteById_invalidId_test() {
        ResponseEntity<String> response = testRestTemplate.exchange(API_USERS + "/" + INVALID_ID, HttpMethod.DELETE, null, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(USER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void saveAll_withArray_test() throws Exception {
        User[] usersToSave = {user3, user4};
        ResponseEntity<String> saveResponse = testRestTemplate.postForEntity(API_USERS + "/createWithArray", usersToSave, String.class);
        then(saveResponse).isNotNull();
        then(saveResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User[] saveResult = objectMapper.readValue(saveResponse.getBody(), new TypeReference<>() {});
        then(saveResult).isEqualTo(usersToSave);
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_USERS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(newUsers);
    }

    @Test
    void saveAll_withList_test() throws Exception {
        List<User> usersToSave = List.of(user3, user4);
        ResponseEntity<String> saveResponse = testRestTemplate.postForEntity(API_USERS + "/createWithList", usersToSave, String.class);
        then(saveResponse).isNotNull();
        then(saveResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<User> saveResult = objectMapper.readValue(saveResponse.getBody(), new TypeReference<>() {});
        then(saveResult).isEqualTo(usersToSave);
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_USERS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(newUsers);
    }

    @Test
    void findByUsername_validUsername_test() {
        ResponseEntity<User> response = testRestTemplate.getForEntity(API_USERS + "/username/" + VALID_USERNAME, User.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(user1);
    }

    @Test
    void findByUsername_invalidUsername_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS + "/username/" + INVALID_USERNAME, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(USERNAME_NOT_FOUND, INVALID_USERNAME));
    }

    @Test
    void updateByUsername_test() {
        User updatedUser = user1;
        updatedUser.setUsername("New Username");
        ResponseEntity<User> updateResponse = testRestTemplate.exchange(API_USERS + "/username/" + VALID_USERNAME, HttpMethod.PUT, new HttpEntity<>(user1), User.class);
        then(updateResponse).isNotNull();
        then(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(updateResponse.getBody()).isEqualTo(updatedUser);
        ResponseEntity<User> getResponse = testRestTemplate.getForEntity(API_USERS + "/" + VALID_ID, User.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(getResponse.getBody()).isEqualTo(updatedUser);
    }

    @Test
    void deleteByUsername_validUsername_test() throws Exception {
        ResponseEntity<User> deleteResponse = testRestTemplate.exchange(API_USERS + "/username/" + VALID_USERNAME, HttpMethod.DELETE, null, User.class);
        then(deleteResponse).isNotNull();
        then(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<String> getResponse = testRestTemplate.getForEntity(API_USERS + "/username/" + VALID_USERNAME, String.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(getResponse.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(USERNAME_NOT_FOUND, VALID_USERNAME));
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_USERS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(user2));
    }

    @Test
    void deleteByUsername_invalidUsername_test() {
        ResponseEntity<String> response = testRestTemplate.exchange(API_USERS + "/username/" + INVALID_USERNAME, HttpMethod.DELETE, null, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(USERNAME_NOT_FOUND, INVALID_USERNAME));
    }

    @Test
    void login_success_test() throws Exception {
        user1.setPassword("%23Password1");
        userRepository.save(user1);
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS + "/login?username=Username1&password=%23Password1", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        HttpResponse result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result.getCode()).isEqualTo(httpResponse.getCode());
        then(result.getType()).isEqualTo(httpResponse.getType());
        then(getTimestamp(result.getMessage()) - getTimestamp(httpResponse.getMessage())).isLessThan(MAX_ELAPSED_TIME);
    }

    @Test
    void login_userAlreadyLoggedIn_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS + "/login?username=Username2&password=%23Password2", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo(INVALID_REQUEST + String.format(USER_ALREADY_LOGGED_IN, "Username2"));
    }

    @Test
    void login_userNotFound_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS + "/login?username=Username1&password=%23Password2", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo(INVALID_REQUEST + String.format(INVALID_USER, "Username1", "%23Password2"));
    }

    @Test
    void logout_success_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_OUT, System.nanoTime()));
        ResponseEntity<HttpResponse> response = testRestTemplate.getForEntity(API_USERS + "/logout?username=Username2", HttpResponse.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        HttpResponse result = Objects.requireNonNull(response.getBody());
        then(result.getCode()).isEqualTo(httpResponse.getCode());
        then(result.getType()).isEqualTo(httpResponse.getType());
        then(getTimestamp(result.getMessage()) - getTimestamp(httpResponse.getMessage())).isLessThan(MAX_ELAPSED_TIME);
    }

    @Test
    void logout_userAlreadyLoggedOut_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_USERS + "/logout?username=Username1", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(response.getBody()).isEqualTo(INVALID_REQUEST + String.format(USER_ALREADY_LOGGED_OUT, "Username1"));
    }

    private long getTimestamp(String message) {
        return Long.parseLong(message.substring(message.lastIndexOf(" ") + 1));
    }
}
