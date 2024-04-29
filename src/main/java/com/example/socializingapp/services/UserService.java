package com.example.socializingapp.services;

import com.example.socializingapp.dto.users.UserDto;
import com.example.socializingapp.dto.users.UsersDtoMapper;
import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final UsersDtoMapper usersDtoMapper;

    public UserService(UserRepository userRepository, ProfileService profileService, UsersDtoMapper usersDtoMapper) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.usersDtoMapper = usersDtoMapper;
    }

    public boolean signUpUser(UserDto userDto) {
        User user = usersDtoMapper.fromDto(userDto);
        boolean userExists = userRepository.existsByUsername(user.getUsername());
        if (!userExists) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            user.setPassword(bc.encode(user.getPassword()));
            userRepository.save(user);

            profileService.createProfile(user.getUsername());
        }
        return !userExists;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
