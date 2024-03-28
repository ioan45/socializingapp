package com.example.socializingapp.services;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Friendship sendRequest(User user1, User user2) {

        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(user1, user2);
        if (friendship != null) {
            return null;
        }
        friendship = new Friendship();
        friendship.setSender(user1);
        friendship.setReceiver(user2);
        friendship.setStatus("pending");
        friendship.setStatusTimestamp(new Timestamp(System.currentTimeMillis()));
        friendshipRepository.save(friendship);

        return friendship;
    }

    public void acceptRequest(Friendship friendship) {
        if (friendship.getStatus().equals("accepted")) {
            return;
        }
        friendship.setStatus("accepted");
        friendship.setStatusTimestamp(new Timestamp(System.currentTimeMillis()));
        friendshipRepository.save(friendship);
    }

    public boolean areFriends(User user1, User user2) {
        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(user1, user2);
        return friendship != null && friendship.getStatus().equals("accepted");
    }
}
