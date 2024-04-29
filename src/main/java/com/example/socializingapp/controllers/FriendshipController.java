package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.friends.FriendDto;
import com.example.socializingapp.dto.friends.RequestDto;
import com.example.socializingapp.entities.Friendship;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.services.FriendshipService;
import com.example.socializingapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    private Logger logger;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
        this.logger = LoggerFactory.getLogger(FriendshipController.class);
    }

    @GetMapping("")
    public String showFriends(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<FriendDto> friends = friendshipService.getAllFriendsByUser(authentication.getName());
        List<RequestDto> requests = friendshipService.getAllRequestsByUser(authentication.getName());

        model.addAttribute("friends", friends);
        model.addAttribute("requests", requests);

        logger.info("User [" + authentication.getName() + "] accessed friends and requests list");

        return "friendList";
    }

    @PostMapping("/sendRequest")
    public String sendRequest(@ModelAttribute("username") String receiver, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sender = authentication.getName();
        if (receiver == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User does not exist!");
            logger.info("User [" + sender + "] failed to send friend request. Reason: User does not exist");
            return "redirect:/friends";
        }
        if (receiver.equals(sender)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Can't send request to yourself!");
            logger.info("User [" + sender + "] failed to send friend request. Reason: Request sent to himself");
            return "redirect:/friends";
        }
        boolean requestSent = friendshipService.sendRequest(sender, receiver);
        if (requestSent) {
            logger.info("User [" + sender + "] friend request sent");
            redirectAttributes.addFlashAttribute("successMessage", "Friend request sent successfully!");
        } else {
            logger.info("User [" + sender + "] failed to send friend request. Reason: Request already sent");
            redirectAttributes.addFlashAttribute("errorMessage", "Friend request already sent!");
        }
        return "redirect:/friends";
    }

    @PostMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("friendshipId") Integer friendshipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User [" + authentication.getName() + "] accepted request for friendship [id=" + friendshipId + "]");
        friendshipService.acceptRequest(friendshipId);
        return "redirect:/friends";
    }

    @PostMapping("/declineRequest")
    public String declineRequest(@RequestParam("friendshipId") Integer friendshipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User [" + authentication.getName() + "] declined request for friendship [id=" + friendshipId + "]");
        friendshipService.declineRequest(friendshipId);
        return "redirect:/friends";
    }

    @PostMapping("/deleteFriend")
    public String deleteFriend(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User [" + authentication.getName() + "] removed friend [" + username + "]");
        friendshipService.deleteFriend(authentication.getName(), username);
        return "redirect:/friends";
    }
}

