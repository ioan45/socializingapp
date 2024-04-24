package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.GetPostsResult;
import com.example.socializingapp.dto.posts.PostDto;
import com.example.socializingapp.dto.posts.PostsDtoMapper;
import com.example.socializingapp.entities.Post;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostsController.class)
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final PostsDtoMapper postsDtoMapper = new PostsDtoMapper();

    @MockBean
    private PostsService postsService;

    @Test
    @WithMockUser(username = "ioan45")
    public void testGetFriendsPosts() throws Exception {

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(0, new ArrayList<>(), user1, new ArrayList<>(), "testContent", time, 0);
        GetPostsResult postsResult = new GetPostsResult(List.of(postsDtoMapper.fromPost(post, false)), 1, 1);

        when(postsService.getPosts(true, 0)).thenReturn(postsResult);

        mockMvc.perform(get("/friendsposts").with(csrf())
                        .param("page", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("friendsPosts"))
                .andExpect(model().attributeExists("friendsPosts"));
    }

    @Test
    @WithMockUser(username = "ioan45")
    public void testGetMyPosts() throws Exception {

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Post post = new Post(0, new ArrayList<>(), user1, new ArrayList<>(), "testContent", time, 0);
        GetPostsResult postsResult = new GetPostsResult(List.of(postsDtoMapper.fromPost(post, false)), 1, 1);

        when(postsService.getPosts(false, 0)).thenReturn(postsResult);

        mockMvc.perform(get("/myposts").with(csrf())
                        .param("page", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("myPosts"))
                .andExpect(model().attributeExists("myPosts"));
    }
}
