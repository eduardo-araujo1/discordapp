package com.eduardo.discordapp.dto.request;

import com.eduardo.discordapp.model.Server;

public record ChannelRequestDTO(
        String name,
        Server server
) {
}
