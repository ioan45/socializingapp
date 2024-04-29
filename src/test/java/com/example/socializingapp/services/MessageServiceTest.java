package com.example.socializingapp.services;

import com.example.socializingapp.dto.message.MessageDto;
import com.example.socializingapp.dto.message.MessageDtoMapper;
import com.example.socializingapp.entities.Message;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Mock
    private FriendshipService friendshipService;

    @Mock
    private MessageDtoMapper messageDtoMapper;

    @Test
    public void testGetAllMessages() {

        String username = "senderUser";
        String friendName = "receiverUser";

        User senderUser = new User();
        senderUser.setUsername(username);

        User receiverUser = new User();
        receiverUser.setUsername(friendName);

        List<Message> messages = new ArrayList<>();
        Message message1 = new Message();
        message1.setSender(senderUser);
        message1.setReceiver(receiverUser);
        message1.setContent("Hello!");
        messages.add(message1);

        Message message2 = new Message();
        message2.setSender(receiverUser);
        message2.setReceiver(senderUser);
        message2.setContent("Hi!");
        messages.add(message2);

        List<MessageDto> expectedMessageDtoList = new ArrayList<>();
        MessageDto messageDto1 = new MessageDto();
        messageDto1.setSender(username);
        messageDto1.setReceiver(friendName);
        messageDto1.setContent("Hello!");
        expectedMessageDtoList.add(messageDto1);

        MessageDto messageDto2 = new MessageDto();
        messageDto2.setSender(friendName);
        messageDto2.setReceiver(username);
        messageDto2.setContent("Hi!");
        expectedMessageDtoList.add(messageDto2);

        when(messageRepository.findAllByUsers(username, friendName)).thenReturn(messages);
        doNothing().when(friendshipService).readLastMessage(any(Message.class));
        when(messageDtoMapper.fromMessage(message1)).thenReturn(messageDto1);
        when(messageDtoMapper.fromMessage(message2)).thenReturn(messageDto2);

        List<MessageDto> result = messageService.getAllMessages(username, friendName);

        assertEquals(expectedMessageDtoList.size(), result.size());
        assertEquals(expectedMessageDtoList.get(0).getContent(), result.get(0).getContent());
        assertEquals(expectedMessageDtoList.get(1).getContent(), result.get(1).getContent());
    }

    @Test
    public void testSendMessage() {

        String sender = "senderUser";
        String receiver = "receiverUser";
        String content = "Hello!";

        User senderUser = new User();
        senderUser.setUsername(sender);

        User receiverUser = new User();
        receiverUser.setUsername(receiver);

        Message message = new Message();
        message.setSender(senderUser);
        message.setReceiver(receiverUser);
        message.setContent(content);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        when(userService.getUserByUsername(sender)).thenReturn(senderUser);
        when(userService.getUserByUsername(receiver)).thenReturn(receiverUser);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        messageService.sendMessage(sender, receiver, content);

        verify(messageRepository, times(1)).save(any(Message.class));
        verify(friendshipService, times(1)).newMessageSent(any(Message.class));
    }
}
