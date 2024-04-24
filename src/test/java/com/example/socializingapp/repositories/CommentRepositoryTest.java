package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Comment;
import com.example.socializingapp.entities.Post;
import com.example.socializingapp.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("postgre")
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveComment() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        User user2 = new User();
        user2.setUsername("ioan46");
        user2.setPassword("testPass");
        user2.setEmail("ioan46@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = postRepository.save(new Post(1, null, user1, null, "testContent", time, 0));

        Comment comment = new Comment(0, post, user2, "testContent", time);

        // Act

        int resultedCommentId = commentRepository.save(comment).getCommentId();

        // Assert

        assertNotEquals(0, resultedCommentId);
    }

    @Test
    public void testFindAllByPost() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        User user2 = new User();
        user2.setUsername("ioan46");
        user2.setPassword("testPass");
        user2.setEmail("ioan46@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = postRepository.save(new Post(1, null, user1, null, "testContent", time, 0));

        Comment comment = commentRepository.save(new Comment(0, post, user2, "testContent", time));

        // Act

        List<Comment> result = commentRepository.findAllByPostOrderByTimestampAsc(post);

        // Assert

        assertEquals(comment.getCommentId(), result.get(0).getCommentId());

    }

    @Test
    public void testDeleteByPost() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        User user2 = new User();
        user2.setUsername("ioan46");
        user2.setPassword("testPass");
        user2.setEmail("ioan46@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = postRepository.save(new Post(1, null, user1, null, "testContent", time, 0));

        Comment comment = commentRepository.save(new Comment(0, post, user2, "testContent", time));

        // Act

        commentRepository.deleteByPost(post);
        int commentsCount = commentRepository.findAllByPostOrderByTimestampAsc(post).size();

        // Assert

        assertEquals(0, commentsCount);
    }
}
