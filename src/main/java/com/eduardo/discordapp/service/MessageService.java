package com.eduardo.discordapp.service;

import com.eduardo.discordapp.dto.request.MessageDTO;
import com.eduardo.discordapp.exception.ChannelNotFoundException;
import com.eduardo.discordapp.exception.UserNotFoundException;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Message;
import com.eduardo.discordapp.model.User;
import com.eduardo.discordapp.repository.ChannelRepository;
import com.eduardo.discordapp.repository.MessageRepository;
import com.eduardo.discordapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    public MessageDTO saveMessage(String channelId, MessageDTO messageDTO) {
        Channel channel = channelRepository.findById(UUID.fromString(channelId))
                .orElseThrow(() -> new ChannelNotFoundException("Canal não encontrado"));

        User author = userRepository.findById(UUID.fromString(messageDTO.authorId()))
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        System.out.println("Username do autor: " + author.getUsername());

        Message message = new Message();
        message.setContent(messageDTO.content());
        message.setChannel(channel);
        message.setAuthor(author);


        Message savedMessage = messageRepository.save(message);
        return new MessageDTO(
                savedMessage.getMessageId().toString(),
                savedMessage.getContent(),
                savedMessage.getAuthor().getUserId().toString(),
                savedMessage.getAuthor().getUsername(),
                Instant.now().toString()
        );
    }

    public List<MessageDTO> getMessagesByChannelId(String channelId) {
        Channel channel = channelRepository.findById(UUID.fromString(channelId))
                .orElseThrow(() -> new ChannelNotFoundException("Canal não encontrado"));

        List<Message> messages = messageRepository.findByChannel(channel);
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getMessageId().toString(),
                        message.getContent(),
                        message.getAuthor().getUserId().toString(),
                        message.getAuthor().getUsername(),
                        message.getSentAt().toString()
                ))
                .collect(Collectors.toList());
    }
}

