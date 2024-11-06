package com.eduardo.discordapp.controller;

import com.eduardo.discordapp.dto.request.MessageDTO;
import com.eduardo.discordapp.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/servers/{serverId}/channels/{channelId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @MessageMapping("/servers/{serverId}/channels/{channelId}/messages")
    @SendTo("/topic/servers/{serverId}/channels/{channelId}")
    public MessageDTO handleMessage(
            @DestinationVariable String serverId,
            @DestinationVariable String channelId,
            MessageDTO messageDTO) {

        MessageDTO savedMessage = messageService.saveMessage(channelId, messageDTO);

        log.info("Mensagem salva e enviada para o canal: {} do servidor: {} do userId: {}",
                channelId, serverId, messageDTO.authorId());

        return savedMessage;
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable String channelId) {
        List<MessageDTO> messages = messageService.getMessagesByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}
