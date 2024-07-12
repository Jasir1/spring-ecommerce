package com.xrontech.web.domain.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Condition condition;

    @NotNull
    @Min(1)
    private Double price;

    @NotNull
    @Min(1)
    private Integer qty;

    @NotNull
    private Long categoryId;

}
