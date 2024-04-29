package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query("SELECT f FROM Friendship f WHERE (f.sender = :user1 AND f.receiver = :user2) OR (f.sender = :user2 AND f.receiver = :user1)")
    Friendship findBySenderAndReceiverCustom(User user1, User user2);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE (f.sender = :user OR f.receiver = :user) AND f.status = 'accepted' " +
            "ORDER BY f.lastMessageTimestamp DESC")
    List<Friendship> findAcceptedFriendsByUser(User user);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE f.receiver = :user AND f.status = 'pending' " +
            "ORDER BY f.statusTimestamp DESC")
    List<Friendship> findPendingRequestsByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Friendship f " +
            "WHERE (f.sender = :user1 AND f.receiver = :user2) " +
            "OR (f.sender = :user2 AND f.receiver = :user1)")
    void deleteFriendshipByUsers(@Param("user1") User user1, @Param("user2") User user2);

    Friendship findByFriendshipId(Integer friendshipId);
}
