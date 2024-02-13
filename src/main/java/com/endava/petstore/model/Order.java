package com.endava.petstore.model;

import com.endava.petstore.enums.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long")
    @Positive(message = "Order ID must be positive")
    private Long id;

    @ApiModelProperty(name = "petId", dataType = "long")
    @Positive(message = "Pet ID must be positive")
    @NotNull(message = "Pet ID must not be null")
    private Long petId;

    @ApiModelProperty(name = "quantity", dataType = "int")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @ApiModelProperty(name = "shipDate", dataType = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime shipDate;

    @ApiModelProperty(name = "status", dataType = "string", value = "Order Status")
    @NotNull(message = "Order status must not be null")
    private OrderStatus status;

    @ApiModelProperty(name = "complete", dataType = "boolean")
    private Boolean complete;
}
