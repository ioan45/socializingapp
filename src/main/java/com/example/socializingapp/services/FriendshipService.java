package com.example.socializingapp.services;

import com.example.socializingapp.dto.friends.FriendDto;
import com.example.socializingapp.dto.friends.RequestDto;
import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.Message;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public FriendshipService(FriendshipRepository friendshipRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    public Boolean sendRequest(String username1, String username2) {

        User user1 = userService.getUserByUsername(username1);
        User user2 = userService.getUserByUsername(username2);

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

    public boolean areFriends(String username1, String username2) {
        User user1 = userService.getUserByUsername(username1);
        User user2 = userService.getUserByUsername(username2);

        Friendship friendship = friendshipRepository.findBySenderAndReceiverCustom(user1, user2);
        return friendship != null && friendship.getStatus().equals("accepted");
    }

    public List<FriendDto> getAllFriendsByUser(String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) return Collections.emptyList();

        List<Friendship> friendships = friendshipRepository.findAcceptedFriendsByUser(user);
        List<FriendDto> friends = new ArrayList<FriendDto>();
        for (Friendship friendship : friendships) {
            User sender = friendship.getSender(), receiver = friendship.getReceiver();
            if (sender != null && receiver != null) {
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
        }
        return friends;
    }

    public List<RequestDto> getAllRequestsByUser(String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) return Collections.emptyList();
        List<Friendship> friendshipList = friendshipRepository.findPendingRequestsByUser(user);

        List<RequestDto> requestDtoList = new ArrayList<>();

        for (Friendship friendship : friendshipList) {
            User sender = friendship.getSender();
            if (sender != null) {
                RequestDto requestDto = new RequestDto();
                requestDto.setFriendshipId(friendship.getFriendshipId());
                requestDto.setSenderUsername(friendship.getSender().getUsername());

                requestDtoList.add(requestDto);
            }
        }

        return requestDtoList;
    }

    public void deleteFriend(String username1, String username2) {
        User user1 = userService.getUserByUsername(username1);
        User user2 = userService.getUserByUsername(username2);
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