package com.eduardo.discordapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank
        @Size(min = 3, max = 50, message = "O nome deve conter no minimo 3 letras e no máximo 50.")
        String username,
        @NotBlank
        @Email(message = "Por favor, forneça um endereço de e-mail válido.")
        String email,
        @NotBlank
        @Size(min = 5, max = 20, message = "A senha deve ter entre 5 e 20 caracteres.")
        String password) {
}
