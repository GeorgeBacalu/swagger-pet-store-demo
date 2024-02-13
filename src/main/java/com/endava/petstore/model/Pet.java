package com.endava.petstore.model;

import com.endava.petstore.enums.PetStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long", required = true)
    @Positive(message = "Pet ID must be positive")
    private Long id;

    @ApiModelProperty(name = "name", dataType = "string", example = "doggie", required = true)
    @NotBlank(message = "Pet name must not be blank")
    @Size(min = 3, max = 30, message = "Pet name must be between {min} and {max} characters")
    private String name;

    @ApiModelProperty(name = "category")
    @NotNull(message = "Pet category must not be null")
    private Category category;

    @ApiModelProperty(name = "photoUrls", dataType = "array", required = true)
    @NotNull(message = "Pet photo URLs list must not be null")
    private List<String> photoUrls;

    @ApiModelProperty(name = "tags", dataType = "array", required = true)
    @NotNull(message = "Pet tags list must not be null")
    private List<Tag> tags;

    @ApiModelProperty(name = "status", dataType = "string", value = "status in the store")
    @NotNull(message = "Pet status must not be null")
    private PetStatus status;
}
