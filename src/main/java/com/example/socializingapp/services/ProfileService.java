package com.example.socializingapp.services;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.ProfileRepository;
import com.example.socializingapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Profile getProfileByUserId(Integer userId) {
        return profileRepository.findByUserUserId(userId);
    }

    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return getProfileByUserId(user.getUserId());
        }
        return null;
    }
}
