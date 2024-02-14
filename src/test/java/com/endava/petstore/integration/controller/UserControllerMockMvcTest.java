package com.endava.petstore.integration.controller;

import com.endava.petstore.controller.UserController;
import com.endava.petstore.exception.InvalidRequestException;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Objects;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.UserMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void findAll_test() throws Exception {
        given(userService.findAll()).willReturn(users);
        ResultActions actions = mockMvc.perform(get(API_USERS)).andExpect(status().isOk());
        for (int i = 0; i < users.size(); ++i) {
            assertUser(actions, "$[" + i + "]", users.get(i));
        }
        List<User> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        then(result).isEqualTo(users);
    }

    @Test
    void findById_validId_test() throws Exception {
        given(userService.findById(VALID_ID)).willReturn(user1);
        ResultActions actions = mockMvc.perform(get(API_USERS + "/{id}", VALID_ID)).andExpect(status().isOk());
        assertUserJson(actions, user1);
        User result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), User.class);
        then(result).isEqualTo(user1);
    }

    @Test
    void findById_invalidId_test() throws Exception {
        String message = String.format(USER_NOT_FOUND, INVALID_ID);
        given(userService.findById(INVALID_ID)).willThrow(new ResourceNotFoundException(message));
        mockMvc.perform(get(API_USERS + "/{id}", INVALID_ID))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void save_test() throws Exception {
        given(userService.save(any(User.class))).willReturn(user1);
        ResultActions actions = mockMvc.perform(post(API_USERS)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(user1)))
              .andExpect(status().isCreated());
        assertUserJson(actions, user1);
        User result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), User.class);
        then(result).isEqualTo(user1);
    }

    @Test
    void update_test() throws Exception {
        given(userService.update(any(User.class))).willReturn(user2);
        ResultActions actions = mockMvc.perform(put(API_USERS)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(user2)))
              .andExpect(status().isOk());
        assertUserJson(actions, user2);
        User result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), User.class);
        then(result).isEqualTo(user2);
    }

    @Test
    void deleteById_validId_test() throws Exception {
        mockMvc.perform(delete(API_USERS + "/{id}", VALID_ID)).andExpect(status().isNoContent()).andReturn();
        verify(userService).deleteById(VALID_ID);
    }

    @Test
    void deleteById_invalidId_test() throws Exception {
        String message = String.format(USER_NOT_FOUND, INVALID_ID);
        doThrow(new ResourceNotFoundException(message)).when(userService).deleteById(INVALID_ID);
        mockMvc.perform(delete(API_USERS + "/{id}", INVALID_ID))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void saveAll_withArray_test() throws Exception {
        User[] usersToSave = {user3, user4};
        given(userService.saveAll(usersToSave)).willReturn(newUsers);
        ResultActions actions = mockMvc.perform(post(API_USERS + "/createWithArray")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(usersToSave)))
              .andExpect(status().isCreated());
        for (int i = 0; i < newUsers.size(); ++i) {
            assertUser(actions, "$[" + i + "]", newUsers.get(i));
        }
        List<User> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        then(result).isEqualTo(newUsers);
    }

    @Test
    void saveAll_withList_test() throws Exception {
        List<User> usersToSave = List.of(user3, user4);
        given(userService.saveAll(usersToSave)).willReturn(newUsers);
        ResultActions actions = mockMvc.perform(post(API_USERS + "/createWithList")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(usersToSave)))
              .andExpect(status().isCreated());
        for (int i = 0; i < newUsers.size(); ++i) {
            assertUser(actions, "$[" + i + "]", newUsers.get(i));
        }
        List<User> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        then(result).isEqualTo(newUsers);
    }

    @Test
    void findByUsername_validUsername_test() throws Exception {
        given(userService.findByUsername(VALID_USERNAME)).willReturn(user1);
        ResultActions actions = mockMvc.perform(get(API_USERS + "/username/{username}", VALID_USERNAME)).andExpect(status().isOk());
        assertUserJson(actions, user1);
        User result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), User.class);
        then(result).isEqualTo(user1);
    }

    @Test
    void findByUsername_invalidUsername_test() throws Exception {
        String message = String.format(USERNAME_NOT_FOUND, INVALID_USERNAME);
        given(userService.findByUsername(INVALID_USERNAME)).willThrow(new ResourceNotFoundException(message));
        mockMvc.perform(get(API_USERS + "/username/{username}", INVALID_USERNAME))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void updateByUsername_test() throws Exception {
        given(userService.updateByUsername(any(User.class), any(String.class))).willReturn(user2);
        ResultActions actions = mockMvc.perform(put(API_USERS + "/username/{username}", VALID_USERNAME)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(user2)))
              .andExpect(status().isOk());
        assertUserJson(actions, user2);
        User result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), User.class);
        then(result).isEqualTo(user2);
    }

    @Test
    void deleteByUsername_validUsername_test() throws Exception {
        mockMvc.perform(delete(API_USERS + "/username/{username}", VALID_USERNAME)).andExpect(status().isNoContent()).andReturn();
        verify(userService).deleteByUsername(VALID_USERNAME);
    }

    @Test
    void deleteByUsername_invalidUsername_test() throws Exception {
        String message = String.format(USERNAME_NOT_FOUND, INVALID_USERNAME);
        doThrow(new ResourceNotFoundException(message)).when(userService).deleteByUsername(INVALID_USERNAME);
        mockMvc.perform(delete(API_USERS + "/username/{username}", INVALID_USERNAME))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void login_success_test() throws Exception {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        given(userService.login("Username1", "#Password1")).willReturn(httpResponse);
        ResultActions actions = mockMvc.perform(get(API_USERS + "/login")
                    .param("username", "Username1")
                    .param("password", "#Password1"))
              .andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value(httpResponse.getCode()))
              .andExpect(jsonPath("$.type").value(httpResponse.getType()))
              .andExpect(jsonPath("$.message").isNotEmpty());
        HttpResponse result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), HttpResponse.class);
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void login_userAlreadyLoggedIn_test() throws Exception {
        String message = String.format(USER_ALREADY_LOGGED_IN, "Username2");
        given(userService.login("Username2", "#Password2")).willThrow(new InvalidRequestException(String.format(message)));
        mockMvc.perform(get(API_USERS + "/login")
                    .param("username", "Username2")
                    .param("password", "#Password2"))
              .andExpect(status().isBadRequest())
              .andExpect(result -> then(result.getResolvedException() instanceof InvalidRequestException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void login_userNotFound_test() throws Exception {
        String message = String.format(INVALID_USER, "Username1", "#Password2");
        given(userService.login("Username1", "#Password2")).willThrow(new InvalidRequestException(message));
        mockMvc.perform(get(API_USERS + "/login")
                    .param("username", "Username1")
                    .param("password", "#Password2"))
              .andExpect(status().isBadRequest())
              .andExpect(result -> then(result.getResolvedException() instanceof InvalidRequestException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void logout_success_test() throws Exception {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_OUT, System.nanoTime()));
        given(userService.logout("Username2")).willReturn(httpResponse);
        ResultActions actions = mockMvc.perform(get(API_USERS + "/logout")
                    .param("username", "Username2"))
              .andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").value(httpResponse.getCode()))
              .andExpect(jsonPath("$.type").value(httpResponse.getType()))
              .andExpect(jsonPath("$.message").isNotEmpty());
        HttpResponse result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), HttpResponse.class);
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void logout_userAlreadyLoggedOut_test() throws Exception {
        String message = String.format(USER_ALREADY_LOGGED_OUT, "Username1");
        given(userService.logout("Username1")).willThrow(new InvalidRequestException(String.format(message)));
        mockMvc.perform(get(API_USERS + "/logout")
                    .param("username", "Username1"))
              .andExpect(status().isBadRequest())
              .andExpect(result -> then(result.getResolvedException() instanceof InvalidRequestException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    private void assertUser(ResultActions actions, String prefix, User user) throws Exception {
        actions.andExpect(jsonPath(prefix + ".id").value(user.getId()))
              .andExpect(jsonPath(prefix + ".username").value(user.getUsername()))
              .andExpect(jsonPath(prefix + ".firstName").value(user.getFirstName()))
              .andExpect(jsonPath(prefix + ".lastName").value(user.getLastName()))
              .andExpect(jsonPath(prefix + ".email").value(user.getEmail()))
              .andExpect(jsonPath(prefix + ".password").value(user.getPassword()))
              .andExpect(jsonPath(prefix + ".phone").value(user.getPhone()))
              .andExpect(jsonPath(prefix + ".status").value(user.getStatus()))
              .andExpect(jsonPath(prefix + ".loggedIn").value(user.isLoggedIn()));
    }

    private void assertUserJson(ResultActions actions, User user) throws Exception {
        actions.andExpect(jsonPath("$.id").value(user.getId()))
              .andExpect(jsonPath("$.username").value(user.getUsername()))
              .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(user.getLastName()))
              .andExpect(jsonPath("$.email").value(user.getEmail()))
              .andExpect(jsonPath("$.password").value(user.getPassword()))
              .andExpect(jsonPath("$.phone").value(user.getPhone()))
              .andExpect(jsonPath("$.status").value(user.getStatus()))
              .andExpect(jsonPath("$.loggedIn").value(user.isLoggedIn()));
    }
}
