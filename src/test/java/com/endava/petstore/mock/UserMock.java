package com.endava.petstore.mock;

import com.endava.petstore.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMock {

    public static List<User> getMockedUsers() {
        return List.of(getMockedUser1(), getMockedUser2());
    }

    public static List<User> getMockedNewUsers() {
        return List.of(getMockedUser1(), getMockedUser2(), getMockedUser3(), getMockedUser4());
    }

    public static User getMockedUser1() {
        return User.builder()
              .id(1L)
              .username("Username1")
              .firstName("Firstname1")
              .lastName("Lastname1")
              .email("Email1@email.com")
              .password("#Password1")
              .phone("+40700 000 001")
              .status(1)
              .isLoggedIn(false)
              .build();
    }

    public static User getMockedUser2() {
        return User.builder()
              .id(2L)
              .username("Username2")
              .firstName("Firstname2")
              .lastName("Lastname2")
              .email("Email2@email.com")
              .password("#Password2")
              .phone("+40700 000 002")
              .status(2)
              .isLoggedIn(true)
              .build();
    }

    public static User getMockedUser3() {
        return User.builder()
              .id(3L)
              .username("Username3")
              .firstName("Firstname3")
              .lastName("Lastname3")
              .email("Email3@email.com")
              .password("#Password3")
              .phone("+40700 000 003")
              .status(3)
              .isLoggedIn(true)
              .build();
    }

    public static User getMockedUser4() {
        return User.builder()
              .id(4L)
              .username("Username4")
              .firstName("Firstname4")
              .lastName("Lastname4")
              .email("Email4@email.com")
              .password("#Password4")
              .phone("+40700 000 004")
              .status(4)
              .isLoggedIn(true)
              .build();
    }
}
