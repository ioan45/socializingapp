package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query("SELECT f FROM Friendship f WHERE (f.sender = :user1 AND f.receiver = :user2) OR (f.sender = :user2 AND f.receiver = :user1)")
    Friendship findBySenderAndReceiverCustom(User user1, User user2);

    Friendship findBySenderAndReceiver(User user1, User user2);
}
