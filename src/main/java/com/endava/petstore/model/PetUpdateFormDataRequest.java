package com.endava.petstore.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetUpdateFormDataRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "name", dataType = "string")
    @ApiParam(value = "Updated name of the pet")
    private String name;

    @ApiModelProperty(notes = "status", dataType = "string")
    @ApiParam(value = "Updated status of the pet")
    private String status;
}
