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
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping()
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable String serverId,
            @PathVariable String channelId,
            @RequestBody MessageDTO messageDTO) {

        log.info("Recebendo mensagem: {} no canal: {} do servidor: {}", messageDTO, channelId, serverId);
        MessageDTO savedMessage = messageService.saveMessage(channelId, messageDTO);

        String destination = "/topic/servers/" + serverId + "/channels/" + channelId;
        messagingTemplate.convertAndSend(destination, savedMessage);

        log.info("Mensagem enviada para o canal: {} do servidor: {}", channelId, serverId);
        return ResponseEntity.ok(savedMessage);
    }

    @MessageMapping("/ws/servers/{serverId}/channels/{channelId}")
    @SendTo("/topic/servers/{serverId}/channels/{channelId}")
    public MessageDTO listenMessages(@DestinationVariable String serverId,
                                     @DestinationVariable String channelId,
                                     MessageDTO messageDTO) {

        log.info("Mensagem recebida no canal: {} do servidor: {}: {}", channelId, serverId, messageDTO);
        return messageDTO;
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable String channelId) {
        List<MessageDTO> messages = messageService.getMessagesByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}
