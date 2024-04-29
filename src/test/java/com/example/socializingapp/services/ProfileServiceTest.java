package com.example.socializingapp.services;

import com.example.socializingapp.dto.profiles.ProfileDto;
import com.example.socializingapp.dto.profiles.ProfileDtoMapper;
import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.ProfileRepository;
import com.example.socializingapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileDtoMapper profileDtoMapper;

    @Test
    public void testGetProfileByUserId() {
        Integer userId = 1;
        Profile expectedProfile = new Profile();
        when(profileRepository.findByUserUserId(userId)).thenReturn(expectedProfile);

        Profile result = profileService.getProfileByUserId(userId);

        assertEquals(expectedProfile, result);
    }

    @Test
    public void testCreateProfile() {
        String username = "john_doe";
        User user = new User();
        user.setUserId(1);
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Profile savedProfile = new Profile();
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);

        profileService.createProfile(username);

        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    public void testGetProfileByUsername_WhenUserExists() {
        String username = "john_doe";
        User user = new User();
        user.setUserId(1);
        user.setUsername(username);
        Profile profile = new Profile();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserUserId(user.getUserId())).thenReturn(profile);
        ProfileDto expectedDto = new ProfileDto();
        when(profileDtoMapper.getProfileDto(profile)).thenReturn(expectedDto);

        ProfileDto result = profileService.getProfileByUsername(username);

        assertEquals(expectedDto, result);
    }

    @Test
    public void testGetProfileByUsername_WhenUserDoesNotExist() {
        String username = "john_doe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ProfileDto result = profileService.getProfileByUsername(username);

        assertEquals(null, result);
    }

    @Test
    void testSaveProfile() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername("test_user");
        profileDto.setDescription("Software Engineer");
        profileDto.setWebsite("https://example.com");
        profileDto.setBirthday(Date.valueOf("1990-01-01"));
        Timestamp expectedCreationDate = new Timestamp(System.currentTimeMillis());
        profileDto.setCreationDate(expectedCreationDate);

        User user = new User();
        user.setUsername("test_user");
        user.setUserId(1);

        when(userRepository.findByUsername(profileDto.getUsername())).thenReturn(Optional.of(user));

        Profile existingProfile = new Profile();
        existingProfile.setUser(user);
        existingProfile.setDescription("Some old description");
        existingProfile.setWebsite("https://oldwebsite.com");
        existingProfile.setBirthday(Date.valueOf("1980-01-01"));
        Timestamp existingCreationDate = new Timestamp(System.currentTimeMillis());
        existingProfile.setCreationDate(existingCreationDate);

        when(profileRepository.findByUserUserId(user.getUserId())).thenReturn(existingProfile);

        profileService.saveProfile(profileDto);

        verify(profileRepository, times(1)).save(existingProfile);
        assertEquals("Software Engineer", existingProfile.getDescription());
        assertEquals("https://example.com", existingProfile.getWebsite());
        assertEquals(Date.valueOf("1990-01-01"), existingProfile.getBirthday());
        // Assert with a tolerance of 200 milliseconds
        assertEquals(expectedCreationDate.getTime(), existingProfile.getCreationDate().getTime(), 200);
    }


}
