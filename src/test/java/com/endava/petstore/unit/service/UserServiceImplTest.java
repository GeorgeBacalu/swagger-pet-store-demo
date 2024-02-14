package com.endava.petstore.unit.service;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.repository.UserRepository;
import com.endava.petstore.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.UserMock.*;
import static com.endava.petstore.mock.UserMock.getMockedNewUsers;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

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
        given(userRepository.findAll()).willReturn(users);
        List<User> result = userService.findAll();
        then(result).isEqualTo(users);
    }

    @Test
    void findById_test() {
        given(userRepository.findById(VALID_ID)).willReturn(user1);
        User result = userService.findById(VALID_ID);
        then(result).isEqualTo(user1);
    }

    @Test
    void save_test() {
        given(userRepository.save(any(User.class))).willReturn(user1);
        User result = userService.save(user1);
        verify(userRepository).save(userCaptor.capture());
        then(result).isEqualTo(userCaptor.getValue());
    }

    @Test
    void update_test() {
        given(userRepository.update(any(User.class))).willReturn(user2);
        User result = userService.update(user1);
        then(result).isEqualTo(user2);
    }

    @Test
    void deleteById_test() {
        userService.deleteById(VALID_ID);
        verify(userRepository).deleteById(VALID_ID);
    }

    @Test
    void saveAll_withArray_test() {
        User[] usersToSave = {user3, user4};
        given(userRepository.saveAll(usersToSave)).willReturn(newUsers);
        List<User> result = userService.saveAll(usersToSave);
        then(result).isEqualTo(newUsers);
    }

    @Test
    void saveAll_withList_test() {
        List<User> usersToSave = List.of(user3, user4);
        given(userRepository.saveAll(usersToSave)).willReturn(newUsers);
        List<User> result = userService.saveAll(usersToSave);
        then(result).isEqualTo(newUsers);
    }

    @Test
    void findByUsername_test() {
        given(userRepository.findByUsername(VALID_USERNAME)).willReturn(user1);
        User result = userService.findByUsername(VALID_USERNAME);
        then(result).isEqualTo(user1);
    }

    @Test
    void updateByUsername_test() {
        given(userRepository.updateByUsername(any(User.class), any(String.class))).willReturn(user2);
        User result = userService.updateByUsername(user1, VALID_USERNAME);
        then(result).isEqualTo(user2);
    }

    @Test
    void deleteByUsername_test() {
        userService.deleteByUsername(VALID_USERNAME);
        verify(userRepository).deleteByUsername(VALID_USERNAME);
    }

    @Test
    void login_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        given(userRepository.login("Username1", "#Password1")).willReturn(httpResponse);
        HttpResponse result = userService.login("Username1", "#Password1");
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void logout_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_OUT, System.nanoTime()));
        given(userRepository.logout("Username2")).willReturn(httpResponse);
        HttpResponse result = userService.logout("Username2");
        then(result).isEqualTo(httpResponse);
    }
}
