package com.endava.petstore.model;

import com.endava.petstore.enums.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    @Positive(message = "Pet ID must be positive")
    private Long id;

    @NotBlank(message = "Pet name must not be blank")
    @Size(min = 3, max = 30, message = "Pet name must be between {min} and {max} characters")
    private String name;

    @NotNull(message = "Pet category must not be null")
    private Category category;

    @NotNull(message = "Pet photo URLs list must not be null")
    private List<String> photoUrls;

    @NotNull(message = "Pet tags list must not be null")
    private List<Tag> tags;

    @NotNull(message = "Pet status must not be null")
    private PetStatus status;
}
