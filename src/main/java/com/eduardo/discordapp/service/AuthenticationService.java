package com.eduardo.discordapp.service;

import com.eduardo.discordapp.config.TokenService;
import com.eduardo.discordapp.dto.request.AuthenticationDTO;
import com.eduardo.discordapp.dto.request.RegisterDTO;
import com.eduardo.discordapp.enums.UserRole;
import com.eduardo.discordapp.exception.InvalidPasswordException;
import com.eduardo.discordapp.exception.UserAlreadyExistsException;
import com.eduardo.discordapp.model.User;
import com.eduardo.discordapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public String login(AuthenticationDTO authenticationDTO) {
        User user = loadUserByUsername(authenticationDTO.email());

        if (user == null || !passwordEncoder.matches(authenticationDTO.password(), user.getPassword())) {
            throw new InvalidPasswordException("Email ou senha inválidos.");
        }
        return tokenService.generateToken(user);
    }

    public void register(RegisterDTO registerDTO) {
        String email = registerDTO.email();
        if (userRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistsException("O usuário com o e-mail " + email + " já existe.");
        }
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());

        User newUser = new User();
        newUser.setUsername(registerDTO.username());
        newUser.setEmail(email);
        newUser.setPassword(encryptedPassword);
        newUser.setRole(UserRole.USER);
        userRepository.save(newUser);
    }
}
