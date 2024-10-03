package com.eduardo.discordapp.repository;

import com.eduardo.discordapp.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel,UUID> {

}
