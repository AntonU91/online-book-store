package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StatusDto {
    @NotEmpty
    private String status;
}
