package com.example.socializingapp.services;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {
    private UserRepository userRepository;
    private final ProfileService profileService;

    public UserService(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    public boolean signUpUser(User user) {
        boolean userExists = userRepository.existsByUsername(user.getUsername());
        if (!userExists) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            user.setPassword(bc.encode(user.getPassword()));
            userRepository.save(user);
            Profile profile = new Profile();
            profile.setUser(user);
            profile.setCreationDate(new Timestamp(System.currentTimeMillis()));
            profileService.createProfile(profile);
        }
        return !userExists;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
