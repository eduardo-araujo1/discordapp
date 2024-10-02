package com.eduardo.discordapp.service;

import com.eduardo.discordapp.converter.ServerConverter;
import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.model.Server;
import com.eduardo.discordapp.model.User;
import com.eduardo.discordapp.repository.ServerRepository;
import com.eduardo.discordapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;
    private final UserRepository userRepository;
    private final ServerConverter converter;

    public ServerResponseDTO registerServer(ServerRequestDTO requestDTO) {

        User owner = userRepository.findById(UUID.fromString(requestDTO.userId()))
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado."));

        Server newServer = converter.toModel(requestDTO, owner);
        Server savedServer = serverRepository.save(newServer);
        return converter.toDto(savedServer);
    }

    public List<ServerResponseDTO> findAll() {
        List<Server> servers = serverRepository.findAll();

        return servers.stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    public void deleteServer(String serverId, String userId) {
        Server server = serverRepository.findById(UUID.fromString(serverId))
                .orElseThrow(() -> new RuntimeException("Servidor não encontrado"));

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        if (!isUserAdminForServer(user, server)) {
            throw new RuntimeException("Acesso negado: Apenas o administrador do servidor pode realizar esta ação.");
        }

        serverRepository.delete(server);
    }

    public boolean isUserAdminForServer(User user, Server server) {
        return server.getOwner().equals(user);
    }
}
