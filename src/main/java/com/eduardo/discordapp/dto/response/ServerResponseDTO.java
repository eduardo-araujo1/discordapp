package com.eduardo.discordapp.dto.response;

import com.eduardo.discordapp.model.Channel;

import java.time.LocalDateTime;
import java.util.List;

public record ServerResponseDTO(
        String serverName,
        List<Channel> channels,
        LocalDateTime createdAt
) {
}
