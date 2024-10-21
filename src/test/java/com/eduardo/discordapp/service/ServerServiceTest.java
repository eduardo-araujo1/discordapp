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
import com.eduardo.discordapp.util.ServerTestUtil;
import com.eduardo.discordapp.util.UserTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServerConverter converter;

    @InjectMocks
    private ServerService serverService;

    Server serverEntity = ServerTestUtil.createServer();
    ServerRequestDTO serverRequestDTO = ServerTestUtil.createServerRequestDTO();
    ServerResponseDTO serverResponseDTO = ServerTestUtil.createServerResponseDTO();

    User owner = UserTestUtil.createUser();

    @Test
    public void createServer_Success() {
        when(userRepository.findById(UserTestUtil.USER_ID))
                .thenReturn(Optional.of(owner));

        when(converter.toModel(serverRequestDTO, owner)).thenReturn(serverEntity);
        when(serverRepository.save(serverEntity)).thenReturn(serverEntity);
        when(converter.toDto(serverEntity)).thenReturn(serverResponseDTO);

        ServerResponseDTO response = serverService.registerServer(serverRequestDTO);

        assertNotNull(response);
        assertEquals(serverResponseDTO, response);

        verify(userRepository).findById(UserTestUtil.USER_ID);
        verify(converter).toModel(serverRequestDTO, owner);
        verify(serverRepository).save(serverEntity);
        verify(converter).toDto(serverEntity);
    }

    @Test
    public void createServer_UserNotFound() {
        when(userRepository.findById(UserTestUtil.USER_ID)).thenThrow(UserNotFoundException.class);

        assertThatThrownBy(() -> serverService.registerServer(serverRequestDTO)).isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(UserTestUtil.USER_ID);
        verify(serverRepository, never()).save(any(Server.class));
    }

    @Test
    public void findAll_ReturnsListOfServerResponseDTOs() {
        Server server1 = ServerTestUtil.createServer();
        Server server2 = ServerTestUtil.createServer();
        List<Server> serverList = Arrays.asList(server1, server2);

        ServerResponseDTO serverDto = ServerTestUtil.createServerResponseDTO();
        List<ServerResponseDTO> expectedDtoList = Arrays.asList(serverDto, serverDto);

        when(serverRepository.findAll()).thenReturn(serverList);
        when(converter.toDto(any(Server.class))).thenReturn(serverDto);

        List<ServerResponseDTO> result = serverService.findAll();

        assertNotNull(result);
        assertEquals(expectedDtoList.size(), result.size());
        assertThat(result).containsExactlyElementsOf(expectedDtoList);
        verify(serverRepository).findAll();
    }

    @Test
    public void findById_ExistingServer_ReturnsServerResponseDTO() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        Server server = ServerTestUtil.createServer();
        ServerResponseDTO expectedDto = ServerTestUtil.createServerResponseDTO();

        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.of(server));
        when(converter.toDto(server)).thenReturn(expectedDto);
        ServerResponseDTO result = serverService.findById(serverId);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(serverRepository).findById(UUID.fromString(serverId));
        verify(converter).toDto(server);
    }

    @Test
    public void findById_NonExistingServer_ThrowsServerNotFoundException() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serverService.findById(serverId))
                .isInstanceOf(ServerNotFoundException.class)
                .hasMessage("Servidor não encontrado");

        verify(serverRepository).findById(UUID.fromString(serverId));
        verify(converter, never()).toDto(any(Server.class));
    }

    @Test
    public void deleteServer_AdminUser_DeletesServer() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        String userId = UserTestUtil.USER_ID.toString();
        Server server = ServerTestUtil.createServer();
        User adminUser = UserTestUtil.createUser();
        server.setOwner(adminUser);

        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.of(server));
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(adminUser));

        serverService.deleteServer(serverId, userId);

        verify(serverRepository).findById(UUID.fromString(serverId));
        verify(userRepository).findById(UUID.fromString(userId));
        verify(serverRepository).delete(server);
    }

    @Test
    public void deleteServer_NonAdminUser_ThrowsAccessDeniedException() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        String userId = UserTestUtil.USER_ID.toString();
        Server server = ServerTestUtil.createServer();
        User nonAdminUser = UserTestUtil.createUser();
        User adminUser = UserTestUtil.createUser();
        server.setOwner(adminUser);

        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.of(server));
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.of(nonAdminUser));

        assertThatThrownBy(() -> serverService.deleteServer(serverId, userId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Acesso negado: Apenas o administrador do servidor pode realizar esta ação.");

        verify(serverRepository).findById(UUID.fromString(serverId));
        verify(userRepository).findById(UUID.fromString(userId));
        verify(serverRepository, never()).delete(any(Server.class));
    }

    @Test
    public void deleteServer_ServerNotFound_ThrowsServerNotFoundException() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        String userId = UserTestUtil.USER_ID.toString();

        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serverService.deleteServer(serverId, userId))
                .isInstanceOf(ServerNotFoundException.class)
                .hasMessage("Servidor não encontrado");

        verify(serverRepository).findById(UUID.fromString(serverId));
        verify(serverRepository, never()).delete(any(Server.class));
    }

    @Test
    public void deleteServer_UserNotFound_ThrowsUserNotFoundException() {
        String serverId = ServerTestUtil.SERVER_ID.toString();
        String userId = UserTestUtil.USER_ID.toString();
        Server server = ServerTestUtil.createServer();

        when(serverRepository.findById(UUID.fromString(serverId))).thenReturn(Optional.of(server));
        when(userRepository.findById(UUID.fromString(userId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serverService.deleteServer(serverId, userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        verify(serverRepository).findById(UUID.fromString(serverId));
        verify(userRepository).findById(UUID.fromString(userId));
        verify(serverRepository, never()).delete(any(Server.class));
    }

    @Test
    public void isUserAdminForServer_AdminUser_ReturnsTrue() {
        User adminUser = UserTestUtil.createUser();
        Server server = ServerTestUtil.createServer();
        server.setOwner(adminUser);

        boolean result = serverService.isUserAdminForServer(adminUser, server);

        assertTrue(result);
    }

    @Test
    public void isUserAdminForServer_NonAdminUser_ReturnsFalse() {
        User nonAdminUser = UserTestUtil.createUser();
        User adminUser = UserTestUtil.createUser();
        Server server = ServerTestUtil.createServer();
        server.setOwner(adminUser);

        boolean result = serverService.isUserAdminForServer(nonAdminUser, server);

        assertFalse(result);
    }
}
