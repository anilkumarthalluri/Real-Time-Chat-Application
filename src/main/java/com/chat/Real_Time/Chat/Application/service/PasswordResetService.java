package com.chat.Real_Time.Chat.Application.service;

public interface PasswordResetService {
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
