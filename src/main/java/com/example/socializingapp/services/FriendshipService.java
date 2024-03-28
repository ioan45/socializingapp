package com.example.socializingapp.services;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Boolean sendRequest(User user1, User user2) {
        if (user1 == null || user2 == null) {
            return false;
        }
        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(user1, user2);
        if (friendship != null) {
            return false;
        }
        friendship = new Friendship();
        friendship.setSender(user1);
        friendship.setReceiver(user2);
        friendship.setStatus("pending");
        friendship.setStatusTimestamp(new Timestamp(System.currentTimeMillis()));
        friendshipRepository.save(friendship);

        return true;
    }

    public void acceptRequest(Integer friendshipId) {
        Friendship friendship = friendshipRepository.findByFriendshipId(friendshipId);
        if (friendship == null || friendship.getStatus().equals("accepted")) {
            return;
        }
        friendship.setStatus("accepted");
        friendship.setStatusTimestamp(new Timestamp(System.currentTimeMillis()));
        friendshipRepository.save(friendship);
    }

    public void declineRequest(Integer friendshipId) {
        Friendship friendship = friendshipRepository.findByFriendshipId(friendshipId);
        if (friendship == null)
            return;
        friendshipRepository.delete(friendship);
    }

    public boolean areFriends(User user1, User user2) {
        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(user1, user2);
        return friendship != null && friendship.getStatus().equals("accepted");
    }

    public List<User> getAllFriendsByUser(User user) {
        List<Friendship> friendships = friendshipRepository.findAcceptedFriendsByUser(user);
        List<User> friends = new ArrayList<User>();
        for (Friendship friendship : friendships) {
            User sender = friendship.getSender(), receiver = friendship.getReceiver();
            if (sender.getUserId() == user.getUserId()) {
                friends.add(receiver);
            }
            else if (receiver.getUserId() == user.getUserId()) {
                friends.add(sender);
            }
        }
        return friends;
    }

    public List<Friendship> getAllRequestsByUser(User user) {
        return friendshipRepository.findPendingRequestsByUser(user);
    }

    public void deleteFriend(User user1, User user2) {
        friendshipRepository.deleteFriendshipByUsers(user1, user2);
    }
}
