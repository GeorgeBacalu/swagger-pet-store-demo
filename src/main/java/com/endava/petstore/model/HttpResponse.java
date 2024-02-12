package com.endava.petstore.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class HttpResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "code", dataType = "int")
    private Integer code;

    @ApiModelProperty(name = "type", dataType = "string")
    private String type;

    @ApiModelProperty(name = "message", dataType = "string")
    private String message;
}
