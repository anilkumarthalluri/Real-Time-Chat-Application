package com.chat.Real_Time.Chat.Application.controller;

import com.chat.Real_Time.Chat.Application.dto.JwtAuthenticationResponse;
import com.chat.Real_Time.Chat.Application.dto.SignInRequest;
import com.chat.Real_Time.Chat.Application.dto.SignUpRequest;
import com.chat.Real_Time.Chat.Application.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testSignup() {
        SignUpRequest signUpRequest = new SignUpRequest("test", "user", "test@example.com", "password");
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse("token");

        when(authenticationService.signup(any(SignUpRequest.class))).thenReturn(jwtAuthenticationResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signup(signUpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtAuthenticationResponse, response.getBody());
    }

    @Test
    public void testSignin() {
        SignInRequest signInRequest = new SignInRequest("test@example.com", "password");
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse("token");

        when(authenticationService.signin(any(SignInRequest.class))).thenReturn(jwtAuthenticationResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signin(signInRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtAuthenticationResponse, response.getBody());
    }
}