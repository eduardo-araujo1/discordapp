package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotNull String username, @NotNull String email, @NotNull String password) {
}
