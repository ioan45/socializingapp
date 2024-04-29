package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.message.MessageDto;
import com.example.socializingapp.entities.Message;
import com.example.socializingapp.repositories.MessageRepository;
import com.example.socializingapp.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private Logger logger;
    @Autowired
    public MessageController(MessageService messageService) {
        this.logger = LoggerFactory.getLogger(MessageController.class);
        this.messageService = messageService;
    }

    @GetMapping("/{friend}")
    public String showMessages(Model model, @PathVariable("friend") String friendName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<MessageDto> messagesList = messageService.getAllMessages(username, friendName);
        model.addAttribute("messageList", messagesList);

        model.addAttribute("loggedInUser", username);
        model.addAttribute("friend", friendName);

        logger.info("User [" + username + "] accessed chat with friend [" + friendName + "]");

        return "messagePage";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("sender") String sender,
                              @RequestParam("receiver") String receiver,
                              @RequestParam("messageText") String content) {

        messageService.sendMessage(sender, receiver, content);
        logger.info("User [" + sender + "] sent message to friend [" + receiver + "]");

        return "redirect:/message/" + receiver;
    }

}
