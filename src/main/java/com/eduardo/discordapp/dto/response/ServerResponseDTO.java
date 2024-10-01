package com.eduardo.discordapp.dto.response;

import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.User;

import java.time.LocalDateTime;
import java.util.List;

public record ServerResponseDTO(
        String serverName,
        User owner,
        List<Channel> channels,
        LocalDateTime createdAt
) {
}
