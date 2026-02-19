package com.nurzhigit.amanov.qazcodetask.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogRequestDto {

    @NotBlank
    private String path;

    @NotBlank
    private String method;

    @NotNull
    private Integer status;

    @NotNull
    private Long duration;

    private String userId;
}

