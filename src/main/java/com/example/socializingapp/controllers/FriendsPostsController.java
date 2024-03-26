package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.posts.LikeDto;
import com.example.socializingapp.dto.posts.PostDto;
import com.example.socializingapp.services.FriendsPostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/friendsposts")
public class FriendsPostsController {

    private FriendsPostsService friendsPostsService;

    public FriendsPostsController(FriendsPostsService friendsPostsService) {
        this.friendsPostsService = friendsPostsService;
    }

    @GetMapping("")
    public String getFriendsPosts(
            Model model
    ) {
        List<PostDto> friendsPosts = friendsPostsService.getFriendsPosts();
        model.addAttribute("friendsPosts", friendsPosts);
        return "friendsPosts";
    }
}
