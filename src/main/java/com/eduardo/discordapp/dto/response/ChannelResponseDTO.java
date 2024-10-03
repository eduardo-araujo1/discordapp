package com.eduardo.discordapp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public record ChannelResponseDTO(
        String name
) {
}
