package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.PostDto;
import com.example.socializingapp.services.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PostsController {

    private PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/friendsposts")
    public String getFriendsPosts(
            Model model
    ) {
        List<PostDto> friendsPosts = postsService.getPosts(true);
        model.addAttribute("friendsPosts", friendsPosts);
        return "friendsPosts";
    }

    @GetMapping("/myposts")
    public String getMyPosts(
            Model model
    ) {
        List<PostDto> myPosts = postsService.getPosts(false);
        model.addAttribute("myPosts", myPosts);
        return "myPosts";
    }
}
