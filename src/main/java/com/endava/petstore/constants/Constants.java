package com.endava.petstore.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String RESOURCE_NOT_FOUND = "Resource not found: ";
    public static final String INVALID_REQUEST = "Invalid request: ";

    public static final String PET_NOT_FOUND = "Pet with id %s not found";
    public static final String PET_UPDATED = "Pet with id %s was updated";
}
