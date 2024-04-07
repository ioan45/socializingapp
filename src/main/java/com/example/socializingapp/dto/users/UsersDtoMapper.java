package com.example.socializingapp.dto.users;

import com.example.socializingapp.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UsersDtoMapper {
    public User fromDto(UserDto dto) {
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(dto.getPassword());
        newUser.setEmail(dto.getEmail());
        return newUser;
    }
}
