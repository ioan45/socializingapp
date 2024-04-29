package com.example.socializingapp.services;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.ProfileRepository;
import com.example.socializingapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetProfileByUserId() {

        Integer userId = 1;
        Profile expectedProfile = new Profile();
        expectedProfile.setProfileId(1);
        expectedProfile.setUser(new User());
        expectedProfile.setDescription("Test description");

        when(profileRepository.findByUserUserId(userId)).thenReturn(expectedProfile);

        Profile result = profileService.getProfileByUserId(userId);

        assertEquals(expectedProfile, result);
    }

    @Test
    public void testCreateProfile() {

        String username = "testUser";

        User user = new User();
        user.setUserId(1);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        profileService.createProfile(username);

        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    public void testGetProfileByUsername() {

        String username = "testUser";

        User user = new User();
        user.setUserId(1);

        Profile expectedProfile = new Profile();
        expectedProfile.setProfileId(1);
        expectedProfile.setUser(user);
        expectedProfile.setDescription("Test description");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserUserId(user.getUserId())).thenReturn(expectedProfile);

        Profile result = profileService.getProfileByUsername(username);

        assertEquals(expectedProfile, result);
    }

    @Test
    public void testSaveProfile() {

        Profile profile = new Profile();
        profile.setProfileId(1);
        profile.setUser(new User());
        profile.setDescription("Test description");
        profile.setWebsite("www.example.com");

        profileService.saveProfile(profile);

        verify(profileRepository, times(1)).save(profile);
    }
}
