package com.eduardo.discordapp.converter;

import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Server;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerConverter {

    public Server toModel(ServerRequestDTO dto) {
        Server server = new Server();
        server.setServerName(dto.serverName());
        server.setOwner(dto.owner());
        return server;
    }

    public ServerResponseDTO toDto(Server server) {
        return new ServerResponseDTO(
                server.getServerName(),
                server.getOwner(),
                server.getChannels(),
                server.getCreatedAt()
        );
    }
}
