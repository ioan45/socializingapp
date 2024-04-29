package com.example.socializingapp.controllers;

import com.example.socializingapp.dto.profiles.ProfileDto;
import com.example.socializingapp.services.FriendshipService;
import com.example.socializingapp.services.ProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private FriendshipService friendshipService;

    @Test
    @WithMockUser(username = "testUser")
    public void testShowProfile() throws Exception {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername("testUser");
        Mockito.when(profileService.getProfileByUsername("testUser")).thenReturn(profileDto);
        Mockito.when(friendshipService.areFriends("testUser", "testUser")).thenReturn(true);

        mockMvc.perform(get("/profile/testUser").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("myProfile"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testShowProfileFriend() throws Exception {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername("anotherUser");
        Mockito.when(profileService.getProfileByUsername("anotherUser")).thenReturn(profileDto);
        Mockito.when(friendshipService.areFriends("testUser", "anotherUser")).thenReturn(true);

        mockMvc.perform(get("/profile/anotherUser").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("friendProfile"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testShowProfileRedirect() throws Exception {
        Mockito.when(profileService.getProfileByUsername("nonExistentUser")).thenReturn(null);

        mockMvc.perform(get("/profile/nonExistentUser").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDefaultProfileRedirect() throws Exception {
        mockMvc.perform(get("/profile").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/testUser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testShowEditProfile() throws Exception {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername("testUser");
        Mockito.when(profileService.getProfileByUsername("testUser")).thenReturn(profileDto);

        mockMvc.perform(get("/profile/edit").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("editProfile"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testEditProfile() throws Exception {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername("testUser");
        Mockito.when(profileService.getProfileByUsername("testUser")).thenReturn(profileDto);

        mockMvc.perform(post("/profile/edit")
                        .param("description", "Test description")
                        .param("website", "https://example.com")
                        .param("birthday", "1990-01-01")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        Mockito.verify(profileService, Mockito.times(1)).saveProfile(
                argThat(savedProfile -> savedProfile.getDescription().equals("Test description")
                        && savedProfile.getWebsite().equals("https://example.com")
                        && savedProfile.getBirthday().equals(Date.valueOf("1990-01-01")))
        );
    }
}
