package com.example.socializingapp.services;

import com.example.socializingapp.dto.friends.FriendDto;
import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.Message;
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
        friendship.setNewMessage(false);
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

    public List<FriendDto> getAllFriendsByUser(User user) {
        List<Friendship> friendships = friendshipRepository.findAcceptedFriendsByUser(user);
        List<FriendDto> friends = new ArrayList<FriendDto>();
        for (Friendship friendship : friendships) {
            User sender = friendship.getSender(), receiver = friendship.getReceiver();
            FriendDto friendDto = new FriendDto();
            friendDto.setBold(false);
            if (sender.getUserId() == user.getUserId()) {
                friendDto.setUsername(receiver.getUsername());
            } else if (receiver.getUserId() == user.getUserId()) {
                friendDto.setUsername(sender.getUsername());
            }
            if (friendship.getLastSender() != null && !friendship.getLastSender().equals(user.getUsername()) && friendship.getNewMessage()) {
                friendDto.setBold(true);
            }
            friends.add(friendDto);
        }
        return friends;
    }

    public List<Friendship> getAllRequestsByUser(User user) {
        return friendshipRepository.findPendingRequestsByUser(user);
    }

    public void deleteFriend(User user1, User user2) {
        friendshipRepository.deleteFriendshipByUsers(user1, user2);
    }

    public void newMessageSent(Message message) {
        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(message.getSender(), message.getReceiver());
        if (friendship == null) {
            return;
        }
        friendship.setLastSender(message.getSender().getUsername());
        friendship.setLastMessageTimestamp(message.getTimestamp());
        friendship.setNewMessage(true);

        friendshipRepository.save(friendship);
    }

    public void readLastMessage(Message message) {
        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(message.getSender(), message.getReceiver());
        if (friendship == null) {
            return;
        }

        friendship.setNewMessage(false);

        friendshipRepository.save(friendship);
    }
}