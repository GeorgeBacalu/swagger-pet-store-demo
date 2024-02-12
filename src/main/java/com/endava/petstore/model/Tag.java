package com.endava.petstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Positive(message = "Tag ID must be positive")
    private Long id;

    @NotBlank(message = "Tag name must not be blank")
    @Size(min = 3, max = 30, message = "Tag name must be between {min} and {max} characters")
    private String name;
}
