package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.CommentDto;
import com.example.socializingapp.dto.posts.LikeDto;
import com.example.socializingapp.repositories.CommentRepository;
import com.example.socializingapp.repositories.PostRepository;
import com.example.socializingapp.services.FriendsPostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friendsposts")
public class FriendsPostsRestController {
    private FriendsPostsService friendsPostsService;

    public FriendsPostsRestController(FriendsPostsService friendsPostsService) {
        this.friendsPostsService = friendsPostsService;
    }

    @PostMapping("/like")
    public ResponseEntity<String> toggleLike(
            @RequestBody LikeDto like
    ) {
        friendsPostsService.toggleLike(like);
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/comment")
    public ResponseEntity<String> postComment(
            @RequestBody CommentDto comment
    ) {
        friendsPostsService.postComment(comment);
        return ResponseEntity.ok().body(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
