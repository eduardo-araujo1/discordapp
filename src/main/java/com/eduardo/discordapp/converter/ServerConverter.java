package com.eduardo.discordapp.converter;

import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.model.Server;
import com.eduardo.discordapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class ServerConverter {

    public Server toModel(ServerRequestDTO dto, User owner) {
        Server server = new Server();
        server.setServerName(dto.serverName());
        server.setOwner(owner);
        return server;
    }

    public ServerResponseDTO toDto(Server server) {
        return new ServerResponseDTO(
                server.getServerName(),
                server.getChannels(),
                server.getCreatedAt()
        );
    }
}
