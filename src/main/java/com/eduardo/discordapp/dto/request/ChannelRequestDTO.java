package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelRequestDTO(
        @NotBlank
        @Size(min = 2, max = 20, message = "O nome deve ter no minímo 2 letras e no máximo 20")
        String name
) {
}
