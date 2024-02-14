package com.endava.petstore.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String RESOURCE_NOT_FOUND = "Resource not found: ";
    public static final String INVALID_REQUEST = "Invalid request: ";

    public static final String PET_NOT_FOUND = "Pet with id %s not found";
    public static final String TAGS_NOT_FOUND = "No tags were provided";
    public static final String ORDER_NOT_FOUND = "Order with id %s not found";
    public static final String USER_NOT_FOUND = "User with id %s not found";
    public static final String USERNAME_NOT_FOUND = "User with username %s not found";
    public static final String INVALID_USER = "User with username %s and password %s not found";
    public static final String USER_ALREADY_LOGGED_IN = "User with username %s already logged in";
    public static final String USER_ALREADY_LOGGED_OUT = "User with username %s already logged out";

    public static final String PET_UPDATED = "Pet with id %s was updated";
    public static final String PET_UPLOADED_IMAGE = "additionalMetadata: %s%nFile uploaded to %s (%s bytes)";
    public static final String USER_LOGGED_IN = "Logged in user session: %s";
    public static final String USER_LOGGED_OUT = "Logged out: %s";

    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = 999L;
    public static final String VALID_USERNAME = "Username1";
    public static final String INVALID_USERNAME = "Username999";
    public static final Long MAX_ELAPSED_TIME = (long) Math.pow(10, 9);

    public static final String API_PETS = "/pet";
    public static final String API_STORE = "/store";
    public static final String API_USERS = "/user";
}
