package com.example.socializingapp.controllers;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.FriendshipService;
import com.example.socializingapp.services.ProfileService;
import com.example.socializingapp.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;
    private final FriendshipService friendshipService;

    public ProfileController(ProfileService profileService, UserService userService, FriendshipService friendshipService) {
        this.profileService = profileService;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    private Integer getUserId(@NotNull Authentication authentication) {
        String username = authentication.getName();
        System.out.println(username);
        User user = userService.getUserByUsername(username);
        if (user != null) {
            System.out.println(user.getUserId());
            return user.getUserId();
        }
        return null;
    }

    @GetMapping("/{username}")
    public String showProfile(@PathVariable String username, Model model) {
        Profile profile = profileService.getProfileByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (profile == null) {
            return "redirect:/";
        }

        model.addAttribute("profile", profile);

        if (username.equals(authentication.getName())) {
            return "myProfile";
        }

        Profile profileLoggedIn = profileService.getProfileByUsername(authentication.getName());
        if(friendshipService.areFriends(profileLoggedIn.getUser(), profile.getUser())) {
            return "friendProfile";
        }

        return "redirect:/";
    }
    @GetMapping("")
    public String defaultProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile/" + authentication.getName();
        }
        return "redirect:/";
    }
}
