package org.example.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotEmpty
    @NotNull
    private String title;

    @NotEmpty
    @NotNull
    private String author;

    @NotEmpty
    @NotNull
    private String isbn;

    @PositiveOrZero
    private BigDecimal price;

    private String description;
    private String coverImage;
}
