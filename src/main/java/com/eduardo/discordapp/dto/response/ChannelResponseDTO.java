package com.eduardo.discordapp.dto.response;

import com.eduardo.discordapp.model.Server;

public record ChannelResponseDTO(
        String name,
        Server server
) {
}
