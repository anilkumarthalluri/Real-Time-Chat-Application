package com.chat.Real_Time.Chat.Application.controller;

import com.chat.Real_Time.Chat.Application.model.ChatMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebSocketEventListenerTest {

    @Mock
    private SimpMessageSendingOperations messagingTemplate;

    @InjectMocks
    private WebSocketEventListener webSocketEventListener;

    @Captor
    private ArgumentCaptor<ChatMessage> chatMessageCaptor;

    @Test
    public void handleWebSocketDisconnectListener() {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create();
        headerAccessor.setSessionId("test-session-id");
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("username", "testuser");
        headerAccessor.setSessionAttributes(sessionAttributes);

        SessionDisconnectEvent event = new SessionDisconnectEvent(this, new GenericMessage<>("", headerAccessor.getMessageHeaders()), "test-session-id", null);

        webSocketEventListener.handleWebSocketDisconnectListener(event);

        verify(messagingTemplate).convertAndSend(ArgumentCaptor.forClass(String.class).capture(), chatMessageCaptor.capture());

        assertEquals("/topic/public", "/topic/public");
        ChatMessage capturedChatMessage = chatMessageCaptor.getValue();
        assertEquals(ChatMessage.MessageType.LEAVE, capturedChatMessage.getType());
        assertEquals("testuser", capturedChatMessage.getSender());
    }
}
