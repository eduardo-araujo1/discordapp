package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.Size;

public record ServerRequestDTO(
        @Size(min = 5, max = 40, message = "A senha deve ter entre 5 e 40 caracteres.")
        String serverName,
        String userId) {
}
