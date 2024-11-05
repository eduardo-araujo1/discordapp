package com.eduardo.discordapp.service;

import com.eduardo.discordapp.converter.ChannelConverter;
import com.eduardo.discordapp.dto.request.ChannelRequestDTO;
import com.eduardo.discordapp.dto.response.ChannelResponseDTO;
import com.eduardo.discordapp.exception.ServerNotFoundException;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Server;
import com.eduardo.discordapp.repository.ChannelRepository;
import com.eduardo.discordapp.repository.ServerRepository;
import com.eduardo.discordapp.util.ChannelTestUtil;
import com.eduardo.discordapp.util.ServerTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private ChannelConverter converter;

    @InjectMocks
    private ChannelService channelService;

    Channel channelEntity = ChannelTestUtil.createChannel();
    ChannelRequestDTO channelRequestDTO = ChannelTestUtil.createChannelRequestDTO();
    ChannelResponseDTO channelResponseDTO = ChannelTestUtil.createChannelResponseDTO();

    Server server = ServerTestUtil.createServer();

    @Test
    @DisplayName("Deve criar um novo canal e retornar ChannelResponseDTO com sucesso")
    public void createChannel_Success() {
        when(serverRepository.findById(ServerTestUtil.SERVER_ID)).thenReturn(Optional.of(server));
        when(converter.toModel(channelRequestDTO, server)).thenReturn(channelEntity);
        when(channelRepository.save(channelEntity)).thenReturn(channelEntity);
        when(converter.toDto(channelEntity)).thenReturn(channelResponseDTO);

        ChannelResponseDTO response = channelService.createChannelForServer(channelRequestDTO, ServerTestUtil.SERVER_ID.toString());

        assertNotNull(response);
        assertEquals(channelResponseDTO, response);

        verify(serverRepository).findById(ServerTestUtil.SERVER_ID);
        verify(converter).toModel(channelRequestDTO, server);
        verify(channelRepository).save(channelEntity);
        verify(converter).toDto(channelEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar canal para servidor inexistente")
    public void createChannel_ServerNotFound() {
        when(serverRepository.findById(ServerTestUtil.SERVER_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                channelService.createChannelForServer(channelRequestDTO, ServerTestUtil.SERVER_ID.toString()))
                .isInstanceOf(ServerNotFoundException.class)
                .hasMessage("Servidor não encontrado");

        verify(serverRepository).findById(ServerTestUtil.SERVER_ID);
        verify(channelRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista de ChannelResponseDTOs para o ID do servidor especificado")
    public void findChannelsByServerId_ReturnsListOfChannelResponseDTOs() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        List<Channel> channelList = Arrays.asList(
                ChannelTestUtil.createChannel(),
                ChannelTestUtil.createChannel()
        );
        Server server = ServerTestUtil.createServer();
        server.setChannels(channelList);

        List<ChannelResponseDTO> expectedDtoList = channelList.stream()
                .map(channel -> ChannelTestUtil.createChannelResponseDTO())
                .collect(Collectors.toList());

        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.of(server));
        when(converter.toDto(any(Channel.class))).thenAnswer(invocation -> {
            Channel channel = invocation.getArgument(0);
            return ChannelTestUtil.createChannelResponseDTO();
        });

        List<ChannelResponseDTO> result = channelService.findChannelsByServerId(serverId);

        assertNotNull(result);
        assertEquals(expectedDtoList.size(), result.size());
        assertEquals(expectedDtoList, result);

        verify(serverRepository).findById(UUID.fromString(serverId));
    }

    @Test
    @DisplayName("Deve retornar ChannelResponseDTO para o ID do canal especificado")
    public void findChannelById_ReturnsChannelResponseDTO() {
        String channelId = ChannelTestUtil.CHANNEL_ID.toString();
        Channel channel = ChannelTestUtil.createChannel();
        ChannelResponseDTO expectedDto = ChannelTestUtil.createChannelResponseDTO();

        when(channelRepository.findById(UUID.fromString(channelId))).thenReturn(Optional.of(channel));
        when(converter.toDto(channel)).thenReturn(expectedDto);

        ChannelResponseDTO result = channelService.findChannelById(channelId);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(channelRepository).findById(UUID.fromString(channelId));
        verify(converter).toDto(channel);
    }

}
