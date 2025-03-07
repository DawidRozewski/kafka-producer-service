package com.example.demo.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageDto(
        @NotBlank(message = "Message content cannot be empty.") String content) {
}
