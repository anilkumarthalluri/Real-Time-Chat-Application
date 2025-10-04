package com.chat.Real_Time.Chat.Application.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}
