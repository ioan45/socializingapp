package com.example.socializingapp.services;

import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean signUpUser(User user) {
        boolean userExists = userRepository.existsByUsername(user.getUsername());
        if (!userExists) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            user.setPassword(bc.encode(user.getPassword()));
            userRepository.save(user);
        }
        return !userExists;
    }
}
