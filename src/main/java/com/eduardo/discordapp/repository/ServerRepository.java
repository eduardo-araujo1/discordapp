package com.eduardo.discordapp.repository;

import com.eduardo.discordapp.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServerRepository extends JpaRepository<UUID, Server> {
}
