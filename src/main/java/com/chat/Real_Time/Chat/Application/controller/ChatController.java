package com.chat.Real_Time.Chat.Application.controller;

import com.chat.Real_Time.Chat.Application.model.ChatMessage;
import com.chat.Real_Time.Chat.Application.repository.ChatMessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(new Date());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setTimestamp(new Date());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.typing")
    @SendTo("/topic/public")
    public ChatMessage handleTyping(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}
