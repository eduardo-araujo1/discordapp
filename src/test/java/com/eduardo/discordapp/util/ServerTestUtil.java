package com.eduardo.discordapp.util;

import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.enums.UserRole;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Server;
import com.eduardo.discordapp.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ServerTestUtil {

    public static UUID SERVER_ID = UUID.randomUUID();
    public static String SERVER_NAME = "Test Server";
    public static User OWNER = UserTestUtil.createUser();
    public static List<Channel> CHANNELS = List.of(new Channel(), new Channel());
    public static LocalDateTime CREATED_AT = LocalDateTime.now().minusDays(1);
    public static LocalDateTime UPDATED_AT = LocalDateTime.now();

    public static Server createServer() {
        Server server = new Server();
        server.setServerId(SERVER_ID);
        server.setServerName(SERVER_NAME);
        server.setOwner(OWNER);
        server.setChannels(CHANNELS);
        server.setCreatedAt(CREATED_AT);
        server.setUpdatedAt(UPDATED_AT);
        return server;
    }

    public static ServerRequestDTO createServerRequestDTO() {
        return new ServerRequestDTO(SERVER_NAME, OWNER.getUserId().toString());
    }

    public static ServerResponseDTO createServerResponseDTO() {
        return new ServerResponseDTO(
                SERVER_ID.toString(),
                SERVER_NAME,
                CHANNELS,
                CREATED_AT
        );
    }
}
