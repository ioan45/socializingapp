package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.Post;
import com.example.socializingapp.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("postgre")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Test
    public void testSavePost() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(0, null, user1, null, "testContent", time, 0);

        userRepository.save(user1);

        // Act

        int resultedPosId = postRepository.save(post).getPostId();

        // Assert

        assertNotEquals(0, resultedPosId);
    }

    @Test
    public void testGetFriendsPosts() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        User user2 = new User();
        user2.setUsername("ioan46");
        user2.setPassword("testPass");
        user2.setEmail("ioan46@gmail.com");

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(1, null, user1, null, "testContent", time, 0);

        userRepository.save(user1);
        userRepository.save(user2);
        friendshipRepository.save(new Friendship(1, user1, user2, "accepted", time, false, time, ""));
        int expectedPosId = postRepository.save(post).getPostId();

        // Act

        Pageable pageable = PageRequest.of(0, 1);
        Page<Post> posts = postRepository.getFriendsPosts(user2.getUsername(), pageable);
        int resultedPostId = posts.getContent().get(0).getPostId();

        // Assert

        assertEquals(expectedPosId, resultedPostId);
    }

    @Test
    public void testGetMyPosts() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(0, null, user1, null, "testContent", time, 0);

        userRepository.save(user1);
        int expectedPostId = postRepository.save(post).getPostId();

        // Act

        Pageable pageable = PageRequest.of(0, 1);
        Page<Post> posts = postRepository.getMyPosts(user1.getUsername(), pageable);
        int resultedPostId = posts.getContent().get(0).getPostId();

        // Assert

        assertEquals(expectedPostId, resultedPostId);
    }
}
