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

    public static final String PET_UPDATED = "Pet with id %s was updated";
    public static final String PET_UPLOADED_IMAGE = "additionalMetadata: %s%nFile uploaded to %s (%s bytes)";

    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = 999L;

    public static final String API_PETS = "/pet";
    public static final String API_STORE = "/store";
}
