package com.eduardo.discordapp.dto.request;

import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.User;

import java.util.List;

public record ServerRequestDTO(
        String serverName,
        User owner) {
}
