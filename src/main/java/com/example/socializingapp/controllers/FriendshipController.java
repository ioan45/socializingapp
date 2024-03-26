package com.example.socializingapp.controllers;

import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.FriendshipService;
import com.example.socializingapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final UserService userService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService, UserService userService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    //Added for debugging
    @GetMapping("/makeFriends/{u1}/{u2}")
    public String makeFriends(@PathVariable("u1") String sender, @PathVariable("u2") String receiver) {
        User uSender = userService.getUserByUsername(sender);
        User uReceiver = userService.getUserByUsername(receiver);

        Friendship friendship = friendshipService.sendRequest(uSender, uReceiver);
        friendshipService.acceptRequest(friendship);
        return "redirect:/";
    }
}
