package com.endava.petstore.unit.repository;

import com.endava.petstore.exception.InvalidRequestException;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.User;
import com.endava.petstore.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.UserMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserRepositoryImpl userRepository;

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
        List<User> result = userRepository.findAll();
        then(result).isEqualTo(users);
    }

    @Test
    void findById_validId_test() {
        given(userRepository.findById(VALID_ID)).willReturn(user1);
        User result = userRepository.findById(VALID_ID);
        then(result).isEqualTo(user1);
    }

    @Test
    void findById_invalidId_test() {
        given(userRepository.findById(INVALID_ID)).willThrow(new ResourceNotFoundException(String.format(USER_NOT_FOUND, INVALID_ID)));
        thenThrownBy(() -> userRepository.findById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void save_test() {
        given(userRepository.save(any(User.class))).willReturn(user1);
        User result = userRepository.save(user1);
        verify(userRepository).save(userCaptor.capture());
        then(result).isEqualTo(userCaptor.getValue());
    }

    @Test
    void update_test() {
        given(userRepository.update(any(User.class))).willReturn(user2);
        User result = userRepository.update(user1);
        then(result).isEqualTo(user2);
    }

    @Test
    void deleteById_validId_test() {
        userRepository.deleteById(VALID_ID);
        verify(userRepository).deleteById(VALID_ID);
    }

    @Test
    void deleteById_invalidId_test() {
        doThrow(new ResourceNotFoundException(String.format(USER_NOT_FOUND, INVALID_ID))).when(userRepository).deleteById(INVALID_ID);
        thenThrownBy(() -> userRepository.deleteById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USER_NOT_FOUND, INVALID_ID));
    }

    @Test
    void saveAll_withArray_test() {
        User[] usersToSave = {user3, user4};
        given(userRepository.saveAll(usersToSave)).willReturn(newUsers);
        List<User> result = userRepository.saveAll(usersToSave);
        then(result).isEqualTo(newUsers);
    }

    @Test
    void saveAll_withList_test() {
        List<User> usersToSave = List.of(user3, user4);
        given(userRepository.saveAll(usersToSave)).willReturn(newUsers);
        List<User> result = userRepository.saveAll(usersToSave);
        then(result).isEqualTo(newUsers);
    }

    @Test
    void findByUsername_validUsername_test() {
        given(userRepository.findByUsername(VALID_USERNAME)).willReturn(user1);
        User result = userRepository.findByUsername(VALID_USERNAME);
        then(result).isEqualTo(user1);
    }

    @Test
    void findByUsername_invalidUsername_test() {
        given(userRepository.findByUsername(INVALID_USERNAME)).willThrow(new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND, INVALID_USERNAME)));
        thenThrownBy(() -> userRepository.findByUsername(INVALID_USERNAME))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USERNAME_NOT_FOUND, INVALID_USERNAME));
    }

    @Test
    void updateByUsername_test() {
        given(userRepository.updateByUsername(any(User.class), any(String.class))).willReturn(user2);
        User result = userRepository.updateByUsername(user1, VALID_USERNAME);
        then(result).isEqualTo(user2);
    }

    @Test
    void deleteByUsername_validUsername_test() {
        userRepository.deleteByUsername(VALID_USERNAME);
        verify(userRepository).deleteByUsername(VALID_USERNAME);
    }

    @Test
    void deleteByUsername_invalidUsername_test() {
        doThrow(new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND, INVALID_USERNAME))).when(userRepository).deleteByUsername(INVALID_USERNAME);
        thenThrownBy(() -> userRepository.deleteByUsername(INVALID_USERNAME))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(USERNAME_NOT_FOUND, INVALID_USERNAME));
    }

    @Test
    void login_success_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_IN, System.nanoTime()));
        given(userRepository.login("Username1", "#Password1")).willReturn(httpResponse);
        HttpResponse result = userRepository.login("Username1", "#Password1");
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void login_userAlreadyLoggedIn_test() {
        given(userRepository.login("Username2", "#Password2")).willThrow(new InvalidRequestException(String.format(USER_ALREADY_LOGGED_IN, "Username2")));
        thenThrownBy(() -> userRepository.login("Username2", "#Password2"))
              .isInstanceOf(InvalidRequestException.class)
              .hasMessage(String.format(USER_ALREADY_LOGGED_IN, "Username2"));
    }

    @Test
    void login_userNotFound_test() {
        given(userRepository.login("Username1", "#Password2")).willThrow(new InvalidRequestException(String.format(INVALID_USER, "Username1", "#Password2")));
        thenThrownBy(() -> userRepository.login("Username1", "#Password2"))
              .isInstanceOf(InvalidRequestException.class)
              .hasMessage(String.format(INVALID_USER, "Username1", "#Password2"));
    }

    @Test
    void logout_success_test() {
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(USER_LOGGED_OUT, System.nanoTime()));
        given(userRepository.logout("Username2")).willReturn(httpResponse);
        HttpResponse result = userRepository.logout("Username2");
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void logout_userAlreadyLoggedOut_test() {
        given(userRepository.logout("Username1")).willThrow(new InvalidRequestException(String.format(USER_ALREADY_LOGGED_OUT, "Username1")));
        thenThrownBy(() -> userRepository.logout("Username1"))
              .isInstanceOf(InvalidRequestException.class)
              .hasMessage(String.format(USER_ALREADY_LOGGED_OUT, "Username1"));
    }
}
