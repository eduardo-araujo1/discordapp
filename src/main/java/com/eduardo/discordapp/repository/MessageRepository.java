package com.eduardo.discordapp.repository;

import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChannel(Channel channel);

}
