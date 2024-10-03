package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
