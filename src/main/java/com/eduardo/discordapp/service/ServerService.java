package com.eduardo.discordapp.service;

import com.eduardo.discordapp.converter.ServerConverter;
import com.eduardo.discordapp.dto.request.ServerRequestDTO;
import com.eduardo.discordapp.dto.response.ServerResponseDTO;
import com.eduardo.discordapp.exception.AccessDeniedException;
import com.eduardo.discordapp.exception.ServerNotFoundException;
import com.eduardo.discordapp.exception.UserNotFoundException;
import com.eduardo.discordapp.model.Server;
import com.eduardo.discordapp.model.User;
import com.eduardo.discordapp.repository.ServerRepository;
import com.eduardo.discordapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .orElseThrow(() -> new UserNotFoundException("Usuario não encontrado."));

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

    public ServerResponseDTO findById(String serverId) {
        Server server = serverRepository.findById(UUID.fromString(serverId))
                .orElseThrow(() -> new ServerNotFoundException("Servidor não encontrado"));

        return converter.toDto(server);
    }

    public void deleteServer(String serverId, String userId) {
        Server server = serverRepository.findById(UUID.fromString(serverId))
                .orElseThrow(() -> new ServerNotFoundException("Servidor não encontrado"));

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));


        if (!isUserAdminForServer(user, server)) {
            throw new AccessDeniedException("Acesso negado: Apenas o administrador do servidor pode realizar esta ação.");
        }

        serverRepository.delete(server);
    }

    public boolean isUserAdminForServer(User user, Server server) {
        return server.getOwner().equals(user);
    }
}
