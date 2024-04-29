package com.example.socializingapp.services;

import com.example.socializingapp.dto.profiles.ProfileDto;
import com.example.socializingapp.dto.profiles.ProfileDtoMapper;
import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.ProfileRepository;
import com.example.socializingapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    private final ProfileDtoMapper profileDtoMapper;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, ProfileDtoMapper profileDtoMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileDtoMapper = profileDtoMapper;
    }

    public Profile getProfileByUserId(Integer userId) {
        return profileRepository.findByUserUserId(userId);
    }

    public void createProfile(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return;

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setCreationDate(new Timestamp(System.currentTimeMillis()));
        profileRepository.save(profile);
    }

    public ProfileDto getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            Profile profile = getProfileByUserId(user.getUserId());
            return profileDtoMapper.getProfileDto(profile);
        }
        return null;
    }

    public void saveProfile(ProfileDto profileDto) {
        User user = userRepository.findByUsername(profileDto.getUsername()).orElse(null);
        if (user == null) return;

        Profile profile = getProfileByUserId(user.getUserId());
        profile.setDescription(profileDto.getDescription());
        profile.setWebsite(profileDto.getWebsite());
        profile.setBirthday(profileDto.getBirthday());
        profileRepository.save(profile);
    }
}
