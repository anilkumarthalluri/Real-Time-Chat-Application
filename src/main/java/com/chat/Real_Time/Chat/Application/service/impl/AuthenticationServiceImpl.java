package com.chat.Real_Time.Chat.Application.service.impl;

import com.chat.Real_Time.Chat.Application.dto.JwtAuthenticationResponse;
import com.chat.Real_Time.Chat.Application.dto.SignInRequest;
import com.chat.Real_Time.Chat.Application.dto.SignUpRequest;
import com.chat.Real_Time.Chat.Application.model.Role;
import com.chat.Real_Time.Chat.Application.model.User;
import com.chat.Real_Time.Chat.Application.repository.UserRepository;
import com.chat.Real_Time.Chat.Application.service.AuthenticationService;
import com.chat.Real_Time.Chat.Application.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        return response;
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        return response;
    }
}
