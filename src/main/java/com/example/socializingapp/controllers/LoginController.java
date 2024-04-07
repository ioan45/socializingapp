package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.users.UserDto;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final Logger logger;
    private UserService userService;

    public LoginController(UserService userService) {
        this.logger = LoggerFactory.getLogger(LoginController.class);
        this.userService = userService;
    }



    @GetMapping("/")
    public String homePage(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile";
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
                return "redirect:/profile";
        }
        if (error != null) {
            model.addAttribute("invalidSignIn", "Invalid username or password!");
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String signUpPage(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile";
        }
        model.addAttribute("userEntity", new User());
        return "signup";
    }

    @PostMapping("/signup/submit")
    public String signUpSubmit(
            @ModelAttribute("userEntity") @Valid UserDto formUser,
            BindingResult bresult,
            Model model
    ) {
        logger.info("Submitted a sign up form! Data: " + formUser.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/profile";

        }

        // Message for invalid form field.
        if (bresult.hasErrors()) {
            model.addAttribute("invalidSignUp", bresult.getAllErrors().get(0).getDefaultMessage());
            return "signup";
        }

        boolean succeeded = userService.signUpUser(formUser);
        if (succeeded) {
            return "redirect:/";
        } else {
            model.addAttribute("invalidSignUp", "Username already exists!");
            return "signup";
        }
    }
}
