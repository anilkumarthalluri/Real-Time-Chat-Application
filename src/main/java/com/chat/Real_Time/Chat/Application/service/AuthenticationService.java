package com.chat.Real_Time.Chat.Application.service;

import com.chat.Real_Time.Chat.Application.dto.JwtAuthenticationResponse;
import com.chat.Real_Time.Chat.Application.dto.SignInRequest;
import com.chat.Real_Time.Chat.Application.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}
