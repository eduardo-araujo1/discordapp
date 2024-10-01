package com.eduardo.discordapp.converter;

import com.eduardo.discordapp.dto.request.ChannelRequestDTO;
import com.eduardo.discordapp.dto.response.ChannelResponseDTO;
import com.eduardo.discordapp.model.Channel;
import org.springframework.stereotype.Component;

@Component
public class ChannelConverter {

    public Channel toModel(ChannelRequestDTO dto) {
        Channel channel = new Channel();
        channel.setName(dto.name());
        channel.setServer(dto.server());
        return channel;
    }

    public ChannelResponseDTO toDto(Channel channel) {
        return new ChannelResponseDTO(
                channel.getName(),
                channel.getServer()
        );
    }
}
