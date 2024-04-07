package com.example.socializingapp.security;

import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private Logger logger;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    }

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {
        if (username.length() < 3 || username.length() > 30 || !username.matches("^[a-zA-Z0-9]+$"))
            throw new UsernameNotFoundException("The provided username is not valid");

        logger.info("Attempted sign in operation for username: " + username);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent())
            return new UserDetailsImpl(user.get());
        else
            throw new UsernameNotFoundException("Invalid Username");
    }
}
