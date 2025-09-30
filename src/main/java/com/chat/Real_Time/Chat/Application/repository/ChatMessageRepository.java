package com.chat.Real_Time.Chat.Application.repository;

import com.chat.Real_Time.Chat.Application.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
