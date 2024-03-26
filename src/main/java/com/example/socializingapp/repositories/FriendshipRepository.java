package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    Friendship findBySenderAndReceiver(User user1, User user2);
}
