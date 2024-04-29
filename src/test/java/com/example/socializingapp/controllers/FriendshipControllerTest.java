package com.example.socializingapp.controllers;

import com.example.socializingapp.controllers.FriendshipController;
import com.example.socializingapp.dto.friends.FriendDto;
import com.example.socializingapp.dto.friends.RequestDto;
import com.example.socializingapp.services.FriendshipService;
import com.example.socializingapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendshipController.class)
public class FriendshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    @Test
    @WithMockUser(username = "testUser")
    public void testSendRequest() throws Exception {
        String receiverUsername = "receiverUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("testUser");
        Mockito.when(friendshipService.sendRequest("testUser", receiverUsername)).thenReturn(true);

        mockMvc.perform(post("/friends/sendRequest")
                        .param("username", receiverUsername)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friends"))
                .andExpect(flash().attribute("successMessage", "Friend request sent successfully!"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testAcceptRequest() throws Exception {
        int friendshipId = 1;

        mockMvc.perform(post("/friends/acceptRequest")
                        .param("friendshipId", String.valueOf(friendshipId))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friends"));

        Mockito.verify(friendshipService, Mockito.times(1)).acceptRequest(friendshipId);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDeclineRequest() throws Exception {
        int friendshipId = 1;

        mockMvc.perform(post("/friends/declineRequest")
                        .param("friendshipId", String.valueOf(friendshipId))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friends"));

        Mockito.verify(friendshipService, Mockito.times(1)).declineRequest(friendshipId);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDeleteFriend() throws Exception {
        String username = "friendUsername";

        mockMvc.perform(post("/friends/deleteFriend")
                        .param("username", username)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friends"));

        Mockito.verify(friendshipService, Mockito.times(1)).deleteFriend("testUser", username);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testShowFriends() throws Exception {
        List<FriendDto> friends = new ArrayList<>();
        friends.add(new FriendDto("friend1", false));
        friends.add(new FriendDto("friend2", true));

        List<RequestDto> requests = new ArrayList<>();
        requests.add(new RequestDto(1, "requester1"));
        requests.add(new RequestDto(2, "requester2"));

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("testUser");
        Mockito.when(friendshipService.getAllFriendsByUser("testUser")).thenReturn(friends);
        Mockito.when(friendshipService.getAllRequestsByUser("testUser")).thenReturn(requests);

        mockMvc.perform(get("/friends").with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("friendList"))
                .andExpect(model().attributeExists("friends", "requests"))
                .andExpect(model().attribute("friends", friends))
                .andExpect(model().attribute("requests", requests));
    }

}
