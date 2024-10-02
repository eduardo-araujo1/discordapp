package com.eduardo.discordapp.controller;

import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.service.ServerService;
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

    private final ServerService service;

    @PostMapping
    public ResponseEntity<ServerResponseDTO> create(@RequestBody ServerRequestDTO dto) {
        var serverDto = service.registerServer(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                .buildAndExpand(dto.serverName()).toUri();
        return ResponseEntity.created(uri).body(serverDto);
    }

    @GetMapping
    public ResponseEntity <List<ServerResponseDTO>> findAll() {
        List<ServerResponseDTO> servers = service.findAll();
        return ResponseEntity.ok(servers);
    }

    @DeleteMapping("/{serverId}/{userId}")
    public ResponseEntity<Void> deleteServer(@PathVariable String serverId, @PathVariable String userId) {
        service.deleteServer(serverId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
