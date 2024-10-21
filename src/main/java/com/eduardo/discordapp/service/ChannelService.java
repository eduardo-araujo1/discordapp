package com.eduardo.discordapp.service;

import com.eduardo.discordapp.converter.ChannelConverter;
import com.eduardo.discordapp.dto.request.ChannelRequestDTO;
import com.eduardo.discordapp.dto.response.ChannelResponseDTO;
import com.eduardo.discordapp.exception.ChannelNotFoundException;
import com.eduardo.discordapp.exception.ServerNotFoundException;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Server;
import com.eduardo.discordapp.repository.ChannelRepository;
import com.eduardo.discordapp.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;
    private final ChannelConverter channelConverter;


    public ChannelResponseDTO createChannelForServer(ChannelRequestDTO channelRequestDTO, String serverId) {
        Server server = serverRepository.findById(UUID.fromString(serverId))
                .orElseThrow(() -> new ServerNotFoundException("Servidor não encontrado"));

        Channel channel = channelConverter.toModel(channelRequestDTO, server);
        Channel savedChannel = channelRepository.save(channel);

        List<Channel> channels = new ArrayList<>(server.getChannels());
        channels.add(savedChannel);
        server.setChannels(channels);

        serverRepository.save(server);

        return channelConverter.toDto(savedChannel);
    }

    public List<ChannelResponseDTO> findChannelsByServerId(String serverId) {
        Server server = serverRepository.findById(UUID.fromString(serverId))
                .orElseThrow(() -> new ServerNotFoundException("Servidor não encontrado"));

        return server.getChannels().stream()
                .map(channelConverter::toDto)
                .collect(Collectors.toList());
    }

    public ChannelResponseDTO findChannelById(String channelId) {
        Channel channel = channelRepository.findById(UUID.fromString(channelId))
                .orElseThrow(() -> new  ChannelNotFoundException("Canal não encontrado"));

        return channelConverter.toDto(channel);
    }
}
