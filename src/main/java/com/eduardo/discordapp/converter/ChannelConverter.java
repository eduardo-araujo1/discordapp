package com.eduardo.discordapp.converter;

import com.eduardo.discordapp.dto.request.ChannelRequestDTO;
import com.eduardo.discordapp.dto.response.ChannelResponseDTO;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Server;
import org.springframework.stereotype.Component;

@Component
public class ChannelConverter {

    public Channel toModel(ChannelRequestDTO dto, Server serverId) {
        Channel channel = new Channel();
        channel.setName(dto.name());
        channel.setServer(serverId);
        return channel;
    }

    public ChannelResponseDTO toDto(Channel channel) {
        return new ChannelResponseDTO(
                channel.getName()
        );
    }
}
