package com.chat.Real_Time.Chat.Application.controller;

import com.chat.Real_Time.Chat.Application.dto.ForgotPasswordRequest;
import com.chat.Real_Time.Chat.Application.dto.ResetPasswordRequest;
import com.chat.Real_Time.Chat.Application.model.User;
import com.chat.Real_Time.Chat.Application.repository.UserRepository;
import com.chat.Real_Time.Chat.Application.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    public PasswordResetController(PasswordResetService passwordResetService, UserRepository userRepository) {
        this.passwordResetService = passwordResetService;
        this.userRepository = userRepository;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found,Please signUp");
        }
        passwordResetService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }
}
