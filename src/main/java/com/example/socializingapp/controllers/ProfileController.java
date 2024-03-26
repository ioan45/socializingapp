package com.example.socializingapp.controllers;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.services.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Integer id, Model model) {
        Profile profile = profileService.getProfileByUserId(id);
        model.addAttribute("profile", profile);
        return "profile";
    }
}
