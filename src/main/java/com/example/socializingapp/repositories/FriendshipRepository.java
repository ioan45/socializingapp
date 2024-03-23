package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
}
