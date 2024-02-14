package com.endava.petstore.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long")
    @Positive(message = "User ID must be positive")
    private Long id;

    @ApiModelProperty(name = "username", dataType = "string")
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 30, message = "Username must be between {min} and {max} characters")
    private String username;

    @ApiModelProperty(name = "firstName", dataType = "string")
    @NotBlank(message = "First name must not be blank")
    @Size(min = 3, max = 30, message = "First name must be between {min} and {max} characters")
    private String firstName;

    @ApiModelProperty(name = "lastName", dataType = "string")
    @NotBlank(message = "Last name must not be blank")
    @Size(min = 3, max = 30, message = "Last name must be between {min} and {max} characters")
    private String lastName;

    @ApiModelProperty(name = "email", dataType = "string")
    @Email
    private String email;

    /**
     * The password is considered valid if all the following constraints are satisfied:
     * It contains at least 8 characters and at most 20 characters.
     * It contains at least one digit.
     * It contains at least one upper case alphabet.
     * It contains at least one lower case alphabet.
     * It contains at least one special character which includes !@#$%&*()-+=^.
     * It does not contain any white space.
     * In the regex expression:
     * ^ represents starting character of the string.
     * (?=.*[0-9]) represents a digit must occur at least once.
     * (?=.*[a-z]) represents a lower case alphabet must occur at least once.
     * (?=.*[A-Z]) represents an upper case alphabet must occur at least once.
     * (?=.*[@#$%^&-+=()]) represents a special character must occur at least once.
     * (?=\\S+$) represents white spaces aren't allowed in the entire string.
     * .{8, 20} represents at least 8 characters and at most 20 characters.
     * $ represents the end of the string.
     */
    @ApiModelProperty(name = "password", dataType = "string")
    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+-=()])(?=\\S+$).{8,30}$")
    private String password;

    /**
     * The regular expression validates Romanian phone numbers exclusively:
     * ^ represents starting character of the string.
     * 00|\+?40|0) matches the country code for Romania, which can start with "00", "+" (optional) followed by "40" or just "0".
     * (7\d{2}|\d{2}[13]|[2-37]\d|8[02-9]|9[0-2]) matches the main phone number, which can be one of the following:
     * - a mobile phone number starting with "7" followed by any 2 digits
     * - a Bucharest landline number, which starts with any 2 digits other than "7", but ending in "1" or "3"
     * - a landline number for other regions in Romania, which starts with any digit other that "0" or "1" followed by any digit
     * - a special service number which starts with "80" or "90", followed by any digit other than "1" ('8[02-9]'or '9[0-2]')
     * \s?\d{3}\s?\d{3} matches the rest of the phone number, which consists of 3 groups of 3 digits separated by optional spaces.
     * $ represents the end of the string.
     */
    @ApiModelProperty(name = "phone", dataType = "string")
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^(00|\\+?40|0)(7\\d{2}|\\d{2}[13]|[2-37]\\d|8[02-9]|9[0-2])\\s?\\d{3}\\s?\\d{3}$")
    private String phone;

    @ApiModelProperty(name = "userStatus", dataType = "int", value = "User Status")
    @NotNull(message = "User status must not be null")
    private Integer status;

    @ApiModelProperty(name = "isLoggedIn", dataType = "boolean")
    private boolean isLoggedIn = false;
}
