package com.eduardo.discordapp.controller;

import com.eduardo.discordapp.dto.request.AuthenticationDTO;
import com.eduardo.discordapp.dto.request.RegisterDTO;
import com.eduardo.discordapp.dto.response.LoginResponseDTO;
import com.eduardo.discordapp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<Void> register (@Valid @RequestBody RegisterDTO dto) {
        authenticationService.register(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody AuthenticationDTO dto) {
        String token = authenticationService.login(dto);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
