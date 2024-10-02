package com.eduardo.discordapp.repository;

import com.eduardo.discordapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {

    User findByEmail(String email);
}
