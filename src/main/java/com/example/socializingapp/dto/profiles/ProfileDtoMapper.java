package com.example.socializingapp.dto.profiles;

import com.example.socializingapp.entities.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileDtoMapper {
    public ProfileDto getProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(profile.getUser().getUsername());
        profileDto.setEmail(profile.getUser().getEmail());
        profileDto.setDescription(profile.getDescription());
        profileDto.setWebsite(profile.getWebsite());
        profileDto.setBirthday(profile.getBirthday());
        profileDto.setCreationDate(profile.getCreationDate());

        return profileDto;
    }
}
