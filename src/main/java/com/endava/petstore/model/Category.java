package com.endava.petstore.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long", required = true)
    @Positive(message = "Category ID must be positive")
    private Long id;

    @ApiModelProperty(name = "name", dataType = "string", required = true)
    @NotBlank(message = "Category name must not be blank")
    @Size(min = 3, max = 30, message = "Category name must be between {min} and {max} characters")
    private String name;
}
