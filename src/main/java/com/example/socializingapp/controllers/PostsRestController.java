package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.CommentDto;
import com.example.socializingapp.dto.posts.CreatePostDto;
import com.example.socializingapp.dto.posts.LikeDto;
import com.example.socializingapp.services.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostsRestController {
    private PostsService postsService;

    public PostsRestController(PostsService postsService) {
        this.postsService = postsService;
    }

    @PostMapping("/post/like")
    public ResponseEntity<String> toggleLike(
            @RequestBody LikeDto like
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new RuntimeException("No authenticated user!");
        postsService.toggleLike(like);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/post/comment")
    public ResponseEntity<String> postComment(
            @RequestBody CommentDto comment
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new RuntimeException("No authenticated user!");
        postsService.postComment(comment);
        return ResponseEntity.ok().body(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/post/delete")
    public ResponseEntity<String> deletePost(
            @RequestBody Integer postId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new RuntimeException("No authenticated user!");
        postsService.deletePost(postId);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/post/create")
    public ResponseEntity<String> createPost(
            @RequestBody CreatePostDto payload
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new RuntimeException("No authenticated user!");
        postsService.createPost(payload);
        return ResponseEntity.ok().body("");
    }
}
