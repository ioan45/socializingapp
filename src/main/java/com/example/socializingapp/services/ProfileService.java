package com.example.socializingapp.services;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile getProfileByUserId(Integer userId) {
        return profileRepository.findByUserUserId(userId);
    }

    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }
}
