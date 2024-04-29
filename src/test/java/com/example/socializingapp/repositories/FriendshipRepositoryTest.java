package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("postgre")
public class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindBySenderAndReceiverCustom() {
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        Friendship friendship = new Friendship();
        friendship.setSender(user1);
        friendship.setReceiver(user2);
        friendshipRepository.save(friendship);

        // Act
        Friendship foundFriendship = friendshipRepository.findBySenderAndReceiverCustom(user1, user2);

        // Assert
        assertNotNull(foundFriendship);
        assertEquals(user1, foundFriendship.getSender());
        assertEquals(user2, foundFriendship.getReceiver());
    }


    @Test
    public void testFindAcceptedFriendsByUser() {
        User user = new User();
        user.setUsername("user");
        userRepository.save(user);

        User friend = new User();
        friend.setUsername("friend");
        userRepository.save(friend);

        Friendship friendship = new Friendship();
        friendship.setSender(user);
        friendship.setReceiver(friend);
        friendship.setStatus("accepted");
        friendship.setLastMessageTimestamp(new Timestamp(System.currentTimeMillis()));

        friendshipRepository.save(friendship);

        List<Friendship> foundFriendships = friendshipRepository.findAcceptedFriendsByUser(user);

        assertFalse(foundFriendships.isEmpty());
        assertEquals(1, foundFriendships.size());
    }



    @Test
    public void testFindPendingRequestsByUser() {
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        Friendship friendship = new Friendship();
        friendship.setSender(user1);
        friendship.setReceiver(user2);
        friendship.setStatus("pending");
        friendshipRepository.save(friendship);

        List<Friendship> pendingRequests = friendshipRepository.findPendingRequestsByUser(user2);

        assertEquals(1, pendingRequests.size());
        assertEquals(user2, pendingRequests.get(0).getReceiver());
    }


    @Test
    public void testFindByFriendshipId() {
        Friendship friendship = new Friendship();
        friendshipRepository.save(friendship);

        Friendship foundFriendship = friendshipRepository.findByFriendshipId(friendship.getFriendshipId());

        assertNotNull(foundFriendship);
        assertEquals(friendship, foundFriendship);
    }
}
