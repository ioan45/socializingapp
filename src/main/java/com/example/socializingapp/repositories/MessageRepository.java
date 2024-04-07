package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE (m.receiver.username = :user1 AND m.sender.username = :user2) " +
            "OR (m.receiver.username = :user2 AND m.sender.username = :user1) ORDER BY m.timestamp")
    List<Message> findAllByUsers(String user1, String user2);
}
