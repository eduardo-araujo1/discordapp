package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
