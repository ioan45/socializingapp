package com.example.socializingapp.controllers;

import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.UserService;
import com.sun.tools.jconsole.JConsoleContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Integer userId = getUserId(authentication);
            if (userId != null) {
                return "redirect:/profile/" + userId;
            }
        }
        return "home";
    }

    @GetMapping("/signin")
    public String signInPage(
            @RequestParam(required = false) Integer error,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Integer userId = getUserId(authentication);
            if (userId != null) {
                return "redirect:/profile/" + userId;
            }
        }
        if (error != null) {
            model.addAttribute("invalidSignIn", "Invalid username or password!");
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Integer userId = getUserId(authentication);
            if (userId != null) {
                return "redirect:/profile/" + userId;
            }
        }
        model.addAttribute("userEntity", new User());
        return "signup";
    }

    @PostMapping("/signup/submit")
    public String signUpSubmit(@ModelAttribute("userEntity") User formUser, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Integer userId = getUserId(authentication);
            if (userId != null) {
                return "redirect:/profile/" + userId;
            }
        }
        boolean succeeded = userService.signUpUser(formUser);
        if (succeeded) {
            return "redirect:/";
        } else {
            model.addAttribute("invalidSignUp", "Username already exists!");
            return "signup";
        }
    }


    @GetMapping("/profile")
    public String signOut(
            Model model
    ) {
        return "profile";
    }
}
