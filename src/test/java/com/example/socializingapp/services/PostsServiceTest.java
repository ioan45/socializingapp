package com.example.socializingapp.services;

import com.example.socializingapp.dto.posts.CommentDto;
import com.example.socializingapp.dto.posts.LikeDto;
import com.example.socializingapp.entities.Comment;
import com.example.socializingapp.entities.Post;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.CommentRepository;
import com.example.socializingapp.repositories.PostRepository;
import com.example.socializingapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest {

    @InjectMocks
    private PostsService postsService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Test
    public void testGetMyPosts() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(1, new ArrayList<>(), user1, new ArrayList<>(), "testContent", time, 0);
        Page<Post> page = new PageImpl<>(List.of(post));

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("ioan45");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
        when(postRepository.getMyPosts(eq(user1.getUsername()), any(Pageable.class))).thenReturn(page);
        when(commentRepository.findAllByPostOrderByTimestampAsc(any(Post.class))).thenReturn(new ArrayList<>());

        // Act

        int resultedPostId = postsService.getPosts(false, 0).getPosts().get(0).getPostId();

        // Assert

        assertEquals(post.getPostId(), resultedPostId);
    }

    @Test
    public void testToggleLike() {

        // Arrange

        LikeDto like = new LikeDto(1, "liked");

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        user1.setPostsLiked(new ArrayList<>());

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(1, new ArrayList<>(), user1, new ArrayList<>(), "testContent", time, 0);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("ioan45");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(userRepository.findByUsername("ioan45")).thenReturn(Optional.of(user1));

        // Act

        postsService.toggleLike(like);

        // Assert

        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testPostComment() {

        // Arrange

        CommentDto comment = new CommentDto(1, "ioan45", "testContent", "");

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(1, new ArrayList<>(), user1, new ArrayList<>(), "testContent", time, 0);

        when(userRepository.findByUsername("ioan45")).thenReturn(Optional.of(user1));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        // Act

        postsService.postComment(comment);

        // Assert

        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
