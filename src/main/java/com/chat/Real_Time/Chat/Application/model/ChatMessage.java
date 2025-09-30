package com.chat.Real_Time.Chat.Application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String sender;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Date timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING,
        STOP_TYPING
    }
}
