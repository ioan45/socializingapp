package com.example.socializingapp.services;

import com.example.socializingapp.dto.message.MessageDto;
import com.example.socializingapp.dto.message.MessageDtoMapper;
import com.example.socializingapp.entities.Message;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageDtoMapper messageDtoMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService, FriendshipService friendshipService, MessageDtoMapper messageDtoMapper) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageDtoMapper = messageDtoMapper;
    }

    public List<MessageDto> getAllMessages(String username, String friendName) {
        List<Message> messageList = messageRepository.findAllByUsers(username, friendName);
        if (!messageList.isEmpty()) {
            Message lastMessage = messageList.get(messageList.size() - 1);
            if (lastMessage.getReceiver().getUsername().equals(username)) {
                friendshipService.readLastMessage(lastMessage);
            }
        }
        List<MessageDto> messageDtoList = new ArrayList<>();
        for (Message message : messageList) {
            messageDtoList.add(messageDtoMapper.fromMessage(message));
        }
        return messageDtoList;
    }

    public void sendMessage(String sender, String receiver, String content) {
        User senderUser = userService.getUserByUsername(sender);
        User receiverUser = userService.getUserByUsername(receiver);

        Message message = new Message();
        message.setSender(senderUser);
        message.setReceiver(receiverUser);
        message.setContent(content);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        messageRepository.save(message);

        friendshipService.newMessageSent(message);
    }
}
