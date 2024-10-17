package com.eduardo.discordapp.controller;

import com.eduardo.discordapp.dto.request.ChannelRequestDTO;
import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ChannelResponseDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.service.ChannelService;
import com.eduardo.discordapp.service.ServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<ServerResponseDTO> create(@Valid @RequestBody ServerRequestDTO dto) {
        var serverDto = serverService.registerServer(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                .buildAndExpand(dto.serverName()).toUri();
        return ResponseEntity.created(uri).body(serverDto);
    }

    @GetMapping
    public ResponseEntity <List<ServerResponseDTO>> findAll() {
        List<ServerResponseDTO> servers = serverService.findAll();
        return ResponseEntity.ok(servers);
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<ServerResponseDTO> findById(@PathVariable String serverId) {
        ServerResponseDTO server = serverService.findById(serverId);
        return ResponseEntity.ok(server);
    }

    @DeleteMapping("/{serverId}/{userId}")
    public ResponseEntity<Void> deleteServer(@PathVariable String serverId, @PathVariable String userId) {
        serverService.deleteServer(serverId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{serverId}/channels")
    public ResponseEntity<ChannelResponseDTO> createChannelForServer(@Valid @RequestBody ChannelRequestDTO dto,
                                                                     @Valid @PathVariable String serverId) {
        ChannelResponseDTO channelResponse = channelService.createChannelForServer(dto, serverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(channelResponse);
    }

    @GetMapping("/{serverId}/channels")
    public ResponseEntity<List<ChannelResponseDTO>> findChannelsByServerId(@PathVariable String serverId) {
        List<ChannelResponseDTO> channels = channelService.findChannelsByServerId(serverId);
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/{serverId}/channels/{channelId}")
    public ResponseEntity<ChannelResponseDTO> findChannelsById(@PathVariable String channelId) {
        ChannelResponseDTO channels = channelService.findChannelById(channelId);
        return ResponseEntity.ok(channels);
    }

}
