package com.eduardo.discordapp.service;

import com.eduardo.discordapp.config.TokenService;
import com.eduardo.discordapp.dto.request.AuthenticationDTO;
import com.eduardo.discordapp.dto.request.RegisterDTO;
import com.eduardo.discordapp.exception.InvalidPasswordException;
import com.eduardo.discordapp.exception.UserAlreadyExistsException;
import com.eduardo.discordapp.model.User;
import com.eduardo.discordapp.repository.UserRepository;
import com.eduardo.discordapp.util.UserTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthenticationService service;

    @Test
    @DisplayName("Deve retornar o usuário ao carregar pelo email")
    public void loadUserByUsername_ShouldReturnUser() {
        String email = UserTestUtil.EMAIL;
        User user = UserTestUtil.createUser();

        when(repository.findByEmail(email)).thenReturn(user);

        User result = service.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(user, result);
        verify(repository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve retornar o token ao fazer login com credenciais válidas")
    public void login_ShouldReturnToken_WhenCredentialsAreValid() {
        AuthenticationDTO authDto = new AuthenticationDTO(UserTestUtil.EMAIL, UserTestUtil.PASSWORD);
        User user = UserTestUtil.createUser();
        String token = "mockToken";

        when(repository.findByEmail(authDto.email())).thenReturn(user);
        when(passwordEncoder.matches(authDto.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn(token);

        String result = service.login(authDto);

        assertNotNull(result);
        assertEquals(token, result);
        verify(repository).findByEmail(authDto.email());
        verify(passwordEncoder).matches(authDto.password(), user.getPassword());
        verify(tokenService).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer login com senha inválida")
    public void login_ShouldThrowException_WhenPasswordIsInvalid() {
        AuthenticationDTO authDto = new AuthenticationDTO(UserTestUtil.EMAIL, "wrongPassword");
        User user = UserTestUtil.createUser();

        when(repository.findByEmail(authDto.email())).thenReturn(user);
        when(passwordEncoder.matches(authDto.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> service.login(authDto));
        verify(repository).findByEmail(authDto.email());
        verify(passwordEncoder).matches(authDto.password(), user.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer login com e-mail inexistente")
    public void login_ShouldThrowException_WhenUserDoesNotExist() {
        AuthenticationDTO authDto = new AuthenticationDTO("nonexistent@example.com", UserTestUtil.PASSWORD);

        when(repository.findByEmail(authDto.email())).thenReturn(null);

        assertThrows(InvalidPasswordException.class, () -> service.login(authDto));
        verify(repository).findByEmail(authDto.email());
    }

    @Test
    @DisplayName("Deve registrar e salvar novo usuário quando o e-mail está disponível")
    public void register_ShouldSaveNewUser_WhenEmailIsAvailable() {
        RegisterDTO registerDto = new RegisterDTO(UserTestUtil.USERNAME, UserTestUtil.EMAIL, UserTestUtil.PASSWORD);
        String encodedPassword = "encodedPassword";

        when(repository.findByEmail(registerDto.email())).thenReturn(null);
        when(passwordEncoder.encode(registerDto.password())).thenReturn(encodedPassword);

        service.register(registerDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(registerDto.username(), savedUser.getUsername());
        assertEquals(registerDto.email(), savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());

        verify(repository).findByEmail(registerDto.email());
        verify(passwordEncoder).encode(registerDto.password());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar um usuário com e-mail já existente")
    public void register_ShouldThrowException_WhenEmailAlreadyExists() {
        RegisterDTO registerDto = new RegisterDTO(UserTestUtil.USERNAME, UserTestUtil.EMAIL, UserTestUtil.PASSWORD);
        User existingUser = UserTestUtil.createUser();

        when(repository.findByEmail(registerDto.email())).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> service.register(registerDto));
        verify(repository).findByEmail(registerDto.email());
    }

}
