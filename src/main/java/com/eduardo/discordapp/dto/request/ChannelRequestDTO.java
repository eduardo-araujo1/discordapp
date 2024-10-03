package com.eduardo.discordapp.dto.request;

import com.eduardo.discordapp.model.Server;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelRequestDTO(
        @NotBlank
        @Size(min = 5, max = 30, message = "O nome deve ter no minímo 5 letras e no máximo 30")
        String name,
        String  serverId
) {
}
