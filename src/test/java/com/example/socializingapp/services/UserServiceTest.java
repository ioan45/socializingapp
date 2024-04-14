package com.example.socializingapp.services;

import com.example.socializingapp.dto.users.UserDto;
import com.example.socializingapp.dto.users.UsersDtoMapper;
import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private UsersDtoMapper usersDtoMapper;

    @Test
    public void testSignUpUser() {

        // Arrange

        UserDto userDto = new UserDto("ioan45", "ioan45@gmail.com", "testPass");

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        when(userRepository.existsByUsername("ioan45")).thenReturn(false);
        when(usersDtoMapper.fromDto(userDto)).thenReturn(user1);

        // Act

        boolean result = userService.signUpUser(userDto);

        // Assert

        verify(userRepository, times(1)).save(any(User.class));
        verify(profileService, times(1)).createProfile(any(Profile.class));
        assertTrue(result);
    }

    @Test
    public void testGetUserByUsername() {

        // Arrange

        User user1 = new User();
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        when(userRepository.findByUsername("ioan45")).thenReturn(Optional.of(user1));

        // Act

        User result = userService.getUserByUsername("ioan45");

        // Assert

        assertEquals("ioan45", result.getUsername());
    }
}
