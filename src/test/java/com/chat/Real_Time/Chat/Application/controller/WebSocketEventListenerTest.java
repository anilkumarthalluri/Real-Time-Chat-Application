package com.chat.Real_Time.Chat.Application.controller;

import com.chat.Real_Time.Chat.Application.model.ChatMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
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
        // Correctly create the header accessor
        SimpMessageHeaderAccessor simpAccessor = SimpMessageHeaderAccessor.create();
        simpAccessor.setSessionId("test-session-id");
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("username", "testuser");
        simpAccessor.setSessionAttributes(sessionAttributes);
        Message<byte[]> message = new GenericMessage<>(new byte[0], simpAccessor.getMessageHeaders());

        // Create the event with the correctly typed message
        SessionDisconnectEvent event = new SessionDisconnectEvent(this, message, "test-session-id", null);

        webSocketEventListener.handleWebSocketDisconnectListener(event);

        // Verify the correct message is sent
        verify(messagingTemplate).convertAndSend(eq("/topic/public"), chatMessageCaptor.capture());

        ChatMessage capturedChatMessage = chatMessageCaptor.getValue();
        assertEquals(ChatMessage.MessageType.LEAVE, capturedChatMessage.getType());
        assertEquals("testuser", capturedChatMessage.getSender());
    }
}
