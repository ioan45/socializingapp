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
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final FriendshipService friendshipService;

    public ProfileController(ProfileService profileService, FriendshipService friendshipService) {
        this.profileService = profileService;
        this.friendshipService = friendshipService;
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

        if(friendshipService.areFriends(authentication.getName(), profile.getUser().getUsername())) {
            return "friendProfile";
        }

        return "redirect:/";
    }
    @GetMapping("")
    public String defaultProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "redirect:/profile/" + authentication.getName();
    }

    @GetMapping("/edit")
    public String showEditProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Profile profile = profileService.getProfileByUsername(username);
        model.addAttribute("profile", profile);
        return "editProfile";
    }

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("profile") Profile profile) {
        profileService.saveProfile(profile);

        return "redirect:/profile";
    }

}
