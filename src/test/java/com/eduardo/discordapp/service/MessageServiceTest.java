package com.eduardo.discordapp.service;

import com.eduardo.discordapp.dto.request.MessageDTO;
import com.eduardo.discordapp.model.Channel;
import com.eduardo.discordapp.model.Message;
import com.eduardo.discordapp.model.User;
import com.eduardo.discordapp.repository.ChannelRepository;
import com.eduardo.discordapp.repository.MessageRepository;
import com.eduardo.discordapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void saveMessage_ShouldSaveAndReturnMessageDTO() {
        String channelId = UUID.randomUUID().toString();
        String authorId = UUID.randomUUID().toString();
        MessageDTO messageDTO = new MessageDTO(UUID.randomUUID().toString(), "Oiiiiiii", authorId, "TestUser", Instant.now().toString());

        Channel channel = new Channel();
        channel.setChannelId(UUID.fromString(channelId));

        User author = new User();
        author.setUserId(UUID.fromString(authorId));
        author.setUsername("TestUser");

        Message savedMessage = new Message();
        savedMessage.setMessageId(UUID.randomUUID());
        savedMessage.setContent("Oiiiiiii");
        savedMessage.setChannel(channel);
        savedMessage.setAuthor(author);
        savedMessage.setSentAt(LocalDateTime.now());

        when(channelRepository.findById(UUID.fromString(channelId))).thenReturn(Optional.of(channel));
        when(userRepository.findById(UUID.fromString(authorId))).thenReturn(Optional.of(author));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);


        MessageDTO result = messageService.saveMessage(channelId, messageDTO);

        assertNotNull(result);
        assertEquals(savedMessage.getMessageId().toString(), result.id());
        assertEquals(authorId, result.authorId());
        assertEquals("TestUser", result.authorName());

        verify(channelRepository).findById(UUID.fromString(channelId));
        verify(userRepository).findById(UUID.fromString(authorId));
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    public void getMessagesByChannelId_ShouldReturnListOfMessageDTOs() {
        String channelId = UUID.randomUUID().toString();
        Channel channel = new Channel();
        channel.setChannelId(UUID.fromString(channelId));

        User author = new User();
        author.setUserId(UUID.randomUUID());
        author.setUsername("TestUser");

        Message message1 = new Message();
        message1.setMessageId(UUID.randomUUID());
        message1.setContent("Message 1");
        message1.setAuthor(author);
        message1.setChannel(channel);
        message1.setSentAt(LocalDateTime.now());

        Message message2 = new Message();
        message2.setMessageId(UUID.randomUUID());
        message2.setContent("Message 2");
        message2.setAuthor(author);
        message2.setChannel(channel);
        message2.setSentAt(LocalDateTime.now());

        List<Message> messages = Arrays.asList(message1, message2);

        when(channelRepository.findById(UUID.fromString(channelId))).thenReturn(Optional.of(channel));
        when(messageRepository.findByChannel(channel)).thenReturn(messages);

        List<MessageDTO> result = messageService.getMessagesByChannelId(channelId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(channelRepository).findById(UUID.fromString(channelId));
        verify(messageRepository).findByChannel(channel);
    }
}
