package com.example.socializingapp.controllers;

import com.example.socializingapp.controllers.MessageController;
import com.example.socializingapp.dto.message.MessageDto;
import com.example.socializingapp.services.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    @WithMockUser(username = "testUser")
    public void testShowMessages() throws Exception {
        List<MessageDto> messagesList = new ArrayList<>();
        messagesList.add(new MessageDto("sender1", "receiver1", "Hello!"));
        messagesList.add(new MessageDto("sender2", "receiver1", "Hi!"));

        Mockito.when(messageService.getAllMessages("testUser", "friend1")).thenReturn(messagesList);

        mockMvc.perform(get("/message/friend1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("messagePage"))
                .andExpect(model().attributeExists("messageList", "loggedInUser", "friend"))
                .andExpect(model().attribute("messageList", messagesList))
                .andExpect(model().attribute("loggedInUser", "testUser"))
                .andExpect(model().attribute("friend", "friend1"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testSendMessage() throws Exception {
        mockMvc.perform(post("/message/send")
                        .param("sender", "senderUser")
                        .param("receiver", "receiverUser")
                        .param("messageText", "Hello!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/message/receiverUser"));

        Mockito.verify(messageService, Mockito.times(1))
                .sendMessage("senderUser", "receiverUser", "Hello!");
    }





}
