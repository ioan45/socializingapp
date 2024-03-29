package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.GetPostsResult;
import com.example.socializingapp.dto.posts.PostDto;
import com.example.socializingapp.services.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostsController {

    private PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/friendsposts")
    public String getFriendsPosts(
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            Model model
    ) {
        GetPostsResult friendsPosts = postsService.getPosts(true, pageNumber - 1);
        model.addAttribute("friendsPosts", friendsPosts.getPosts());
        model.addAttribute("pagesCount", friendsPosts.getTotalPages());
        model.addAttribute("prevPageNumber", pageNumber - 1);
        model.addAttribute("nextPageNumber", pageNumber + 1);
        return "friendsPosts";
    }

    @GetMapping("/myposts")
    public String getMyPosts(
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            Model model
    ) {
        GetPostsResult friendsPosts = postsService.getPosts(false, pageNumber - 1);
        model.addAttribute("myPosts", friendsPosts.getPosts());
        model.addAttribute("pagesCount", friendsPosts.getTotalPages());
        model.addAttribute("prevPageNumber", pageNumber - 1);
        model.addAttribute("nextPageNumber", pageNumber + 1);
        return "myPosts";
    }
}
