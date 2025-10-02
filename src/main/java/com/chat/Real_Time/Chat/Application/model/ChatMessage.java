package com.chat.Real_Time.Chat.Application.model;

import jakarta.persistence.*;

import java.util.Date;

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

    public ChatMessage() {
    }

    public ChatMessage(Long id, String content, String sender, MessageType type, Date timestamp) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING,
        STOP_TYPING
    }
}
