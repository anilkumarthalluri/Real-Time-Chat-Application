package com.chat.Real_Time.Chat.Application.controller;

import com.chat.Real_Time.Chat.Application.model.ChatMessage;
import com.chat.Real_Time.Chat.Application.repository.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatController chatController;

    @Test
    public void testSendMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("testuser");
        chatMessage.setContent("Hello");

        ChatMessage result = chatController.sendMessage(chatMessage);

        verify(chatMessageRepository).save(chatMessage);
        assertEquals("testuser", result.getSender());
        assertEquals("Hello", result.getContent());
    }

    @Test
    public void testAddUser() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("testuser");
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setSessionId("test-session-id");

        ChatMessage result = chatController.addUser(chatMessage, headerAccessor);

        verify(chatMessageRepository).save(chatMessage);
        assertEquals("testuser", headerAccessor.getSessionAttributes().get("username"));
        assertEquals("testuser", result.getSender());
    }

    @Test
    public void testHandleTyping() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("testuser");
        chatMessage.setType(ChatMessage.MessageType.TYPING);

        ChatMessage result = chatController.handleTyping(chatMessage);

        assertEquals("testuser", result.getSender());
        assertEquals(ChatMessage.MessageType.TYPING, result.getType());
    }
}
