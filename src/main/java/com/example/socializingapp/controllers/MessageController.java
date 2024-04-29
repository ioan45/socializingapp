package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.message.MessageDto;
import com.example.socializingapp.entities.Message;
import com.example.socializingapp.repositories.MessageRepository;
import com.example.socializingapp.services.MessageService;
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
    @Autowired
    public MessageController(MessageService messageService) {
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

        return "messagePage";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("sender") String sender,
                              @RequestParam("receiver") String receiver,
                              @RequestParam("messageText") String content) {

        messageService.sendMessage(sender, receiver, content);

        return "redirect:/message/" + receiver;
    }

}
