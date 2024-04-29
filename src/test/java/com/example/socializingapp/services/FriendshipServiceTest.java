package com.example.socializingapp.services;

import com.example.socializingapp.dto.friends.FriendDto;
import com.example.socializingapp.dto.friends.RequestDto;
import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.Message;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.FriendshipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTest {

    @InjectMocks
    private FriendshipService friendshipService;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserService userService;

    @Test
    public void testSendRequest() {

        String username1 = "user1";
        String username2 = "user2";

        User user1 = new User();
        user1.setUsername(username1);

        User user2 = new User();
        user2.setUsername(username2);

        when(userService.getUserByUsername(username1)).thenReturn(user1);
        when(userService.getUserByUsername(username2)).thenReturn(user2);
        when(friendshipRepository.findBySenderAndReceiverCustom(user1, user2)).thenReturn(null);

        boolean result = friendshipService.sendRequest(username1, username2);

        assertTrue(result);
        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    public void testAcceptRequest() {

        Integer friendshipId = 1;

        Friendship friendship = new Friendship();
        friendship.setFriendshipId(friendshipId);
        friendship.setStatus("pending");

        when(friendshipRepository.findByFriendshipId(friendshipId)).thenReturn(friendship);

        friendshipService.acceptRequest(friendshipId);

        assertEquals("accepted", friendship.getStatus());
        verify(friendshipRepository, times(1)).save(friendship);
    }

    @Test
    public void testDeclineRequest() {

        Integer friendshipId = 1;

        Friendship friendship = new Friendship();
        friendship.setFriendshipId(friendshipId);

        when(friendshipRepository.findByFriendshipId(friendshipId)).thenReturn(friendship);

        friendshipService.declineRequest(friendshipId);

        verify(friendshipRepository, times(1)).delete(friendship);
    }

    @Test
    public void testAreFriends() {

        String username1 = "user1";
        String username2 = "user2";

        User user1 = new User();
        user1.setUsername(username1);

        User user2 = new User();
        user2.setUsername(username2);

        Friendship friendship = new Friendship();
        friendship.setStatus("accepted");

        when(userService.getUserByUsername(username1)).thenReturn(user1);
        when(userService.getUserByUsername(username2)).thenReturn(user2);
        when(friendshipRepository.findBySenderAndReceiverCustom(user1, user2)).thenReturn(friendship);

        boolean result = friendshipService.areFriends(username1, username2);

        assertTrue(result);
    }

    @Test
    public void testGetAllFriendsByUser() {
        User user = new User();
        user.setUserId(1);
        when(userService.getUserByUsername("user")).thenReturn(user);

        User user2 = new User();
        user.setUserId(2);

        List<Friendship> friendshipList = new ArrayList<>();
        Friendship friendship = new Friendship();
        friendship.setSender(user);
        friendship.setReceiver(user2);

        friendshipList.add(friendship);

        when(friendshipRepository.findAcceptedFriendsByUser(any())).thenReturn(friendshipList);

        List<FriendDto> result = friendshipService.getAllFriendsByUser("user");

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllRequestsByUser() {

        String username = "user1";

        User user = new User();
        user.setUsername(username);
        User user2 = new User();
        user2.setUsername("user2");

        Friendship friendship = new Friendship();
        friendship.setReceiver(user);
        friendship.setSender(user2);
        friendship.setStatus("pending");

        List<Friendship> friendshipList = new ArrayList<>();
        friendshipList.add(friendship);

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(friendshipRepository.findPendingRequestsByUser(user)).thenReturn(friendshipList);

        List<RequestDto> result = friendshipService.getAllRequestsByUser(username);

        assertEquals(1, result.size());
        assertEquals("user2", result.get(0).getSenderUsername());
    }

    @Test
    public void testDeleteFriend() {

        String username1 = "user1";
        String username2 = "user2";

        User user1 = new User();
        user1.setUsername(username1);

        User user2 = new User();
        user2.setUsername(username2);

        when(userService.getUserByUsername(username1)).thenReturn(user1);
        when(userService.getUserByUsername(username2)).thenReturn(user2);

        friendshipService.deleteFriend(username1, username2);

        verify(friendshipRepository, times(1)).deleteFriendshipByUsers(user1, user2);
    }

    @Test
    public void testNewMessageSent() {
        User user1 = new User();
        User user2 = new User();

        Message message = new Message();
        message.setSender(user1);
        message.setReceiver(user2);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        Friendship friendship = new Friendship();
        friendship.setSender(user1);
        friendship.setReceiver(user2);

        when(friendshipRepository.findBySenderAndReceiverCustom(message.getSender(), message.getReceiver())).thenReturn(friendship);

        friendshipService.newMessageSent(message);

        assertTrue(friendship.getNewMessage());
        assertEquals(message.getSender().getUsername(), friendship.getLastSender());
        assertNotNull(friendship.getLastMessageTimestamp());
        verify(friendshipRepository, times(1)).save(friendship);
    }

    @Test
    public void testReadLastMessage() {

        Message message = new Message();
        message.setSender(new User());
        message.setReceiver(new User());

        Friendship friendship = new Friendship();
        friendship.setNewMessage(true);

        when(friendshipRepository.findBySenderAndReceiverCustom(message.getSender(), message.getReceiver())).thenReturn(friendship);

        friendshipService.readLastMessage(message);

        assertFalse(friendship.getNewMessage());
        verify(friendshipRepository, times(1)).save(friendship);
    }
}
