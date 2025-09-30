package com.chat.Real_Time.Chat.Application.repository;

import com.chat.Real_Time.Chat.Application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
