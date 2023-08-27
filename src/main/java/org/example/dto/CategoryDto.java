package org.example.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryDto {
    @Length(min = 4)
    private String name;

    private String description;
}
