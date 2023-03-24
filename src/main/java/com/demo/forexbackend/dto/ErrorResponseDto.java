package com.demo.forexbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private boolean success;
    private String message;
}