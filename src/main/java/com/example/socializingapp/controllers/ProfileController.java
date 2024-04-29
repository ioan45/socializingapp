package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.profiles.ProfileDto;
import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.FriendshipService;
import com.example.socializingapp.services.ProfileService;
import com.example.socializingapp.services.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final FriendshipService friendshipService;

    private Logger logger;

    public ProfileController(ProfileService profileService, FriendshipService friendshipService) {
        this.profileService = profileService;
        this.friendshipService = friendshipService;
        this.logger = LoggerFactory.getLogger(ProfileController.class);
    }

    @GetMapping("/{username}")
    public String showProfile(@PathVariable String username, Model model) {
        ProfileDto profile = profileService.getProfileByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (profile == null) {
            return "redirect:/";
        }

        model.addAttribute("profile", profile);

        if (username.equals(authentication.getName())) {
            logger.info("User [" + authentication.getName() + "] accessed personal profile");
            return "myProfile";
        }

        if(friendshipService.areFriends(authentication.getName(), profile.getUsername())) {
            logger.info("User [" + authentication.getName() + "] accessed friend [" + profile.getUsername() + "] profile");
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
        ProfileDto profile = profileService.getProfileByUsername(username);
        model.addAttribute("profile", profile);
        logger.info("User [" + authentication.getName() + "] accessed the editing page of his profile");
        return "editProfile";
    }

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("profile") @Valid ProfileDto profile, BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (bindingResult.hasErrors()) {
            logger.warn("Validation error occurred while editing profile for user [" + authentication.getName() + "]");
            return "editProfile";
        }

        profileService.saveProfile(profile);
        logger.info("User [" + authentication.getName() + "] saved profile changes");
        return "redirect:/profile";
    }

}
