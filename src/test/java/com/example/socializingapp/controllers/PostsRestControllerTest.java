package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.CommentDto;
import com.example.socializingapp.dto.posts.CreatePostDto;
import com.example.socializingapp.dto.posts.LikeDto;
import com.example.socializingapp.services.PostsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

@WebMvcTest(PostsRestController.class)
public class PostsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private PostsService postsService;

    @Test
    @WithMockUser(username = "ioan45")
    public void testToggleLike() throws Exception{
        LikeDto req = new LikeDto(1, "liked");

        mockMvc.perform(post("/post/like").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful());

        verify(postsService, times(1)).toggleLike(any(LikeDto.class));
    }

    @Test
    @WithMockUser(username = "ioan45")
    public void testPostComment() throws Exception {
        CommentDto commentDto = new CommentDto(1, "ioan45", "testContent", "");

        mockMvc.perform(post("/post/comment").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("ioan45"));

        verify(postsService, times(1)).postComment(any(CommentDto.class));
    }

    @Test
    @WithMockUser(username = "ioan45")
    public void testDeletePost() throws Exception {
        int postId = 1;
        mockMvc.perform(post("/post/delete").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postId)))
                .andExpect(status().is2xxSuccessful());

        verify(postsService, times(1)).deletePost(anyInt());
    }

    @Test
    @WithMockUser(username = "ioan45")
    public void testCreatePost() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto("testContent");

        mockMvc.perform(post("/post/create").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostDto)))
                .andExpect(status().is2xxSuccessful());

        verify(postsService, times(1)).createPost(any(CreatePostDto.class));
    }
}
