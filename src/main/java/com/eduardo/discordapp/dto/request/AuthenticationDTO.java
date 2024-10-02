package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(@NotNull String email, @NotNull String password) {
}
