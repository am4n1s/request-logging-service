package com.example.requestlog.dto;

import jakarta.validation.constraints.*;

public record LogRequestDto(
        @NotBlank @Size(max = 512) String path,
        @NotBlank @Size(max = 10) String method,
        @NotNull @Min(100) @Max(599) Integer status,
        @NotNull @PositiveOrZero Long duration,
        @Size(max = 100) String userId) {
}
