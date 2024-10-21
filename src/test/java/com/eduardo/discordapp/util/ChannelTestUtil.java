package com.eduardo.discordapp.util;

import com.eduardo.discordapp.dto.request.ChannelRequestDTO;
import com.eduardo.discordapp.dto.response.ChannelResponseDTO;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Server;

import java.util.UUID;

public class ChannelTestUtil {

    public static UUID CHANNEL_ID = UUID.randomUUID();
    public static String CHANNEL_NAME = "Tricolor";
    public static Server SERVER = ServerTestUtil.createServer();


    public static Channel createChannel() {
        Channel channel = new Channel();
        channel.setChannelId(CHANNEL_ID);
        channel.setName(CHANNEL_NAME);
        channel.setServer(SERVER);
        return channel;
    }

    public static ChannelRequestDTO createChannelRequestDTO() {
        return new ChannelRequestDTO(CHANNEL_NAME);
    }

    public static ChannelResponseDTO createChannelResponseDTO() {
        return new ChannelResponseDTO(
                CHANNEL_ID.toString(),
                CHANNEL_NAME
        );
    }
}
