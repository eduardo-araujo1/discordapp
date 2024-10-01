package com.eduardo.discordapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_channels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "channel_id")
    private UUID channelId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

}
