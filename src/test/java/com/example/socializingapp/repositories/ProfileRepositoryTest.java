package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Profile;
import com.example.socializingapp.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("postgre")
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserUserId_WhenProfileExists() {
        User user = new User();
        user.setUsername("john_doe");
        userRepository.save(user);

        Optional<User> optionalUser = userRepository.findByUsername("john_doe");
        assertTrue(optionalUser.isPresent());
        User savedUser = optionalUser.get();

        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setDescription("Software Engineer");
        profile.setWebsite("https://example.com");
        profile.setBirthday(Date.valueOf("1990-01-01"));
        profile.setCreationDate(new Timestamp(System.currentTimeMillis()));
        profileRepository.save(profile);

        Profile foundProfile = profileRepository.findByUserUserId(savedUser.getUserId());

        assertNotNull(foundProfile);
        assertEquals(savedUser, foundProfile.getUser());
        assertEquals("Software Engineer", foundProfile.getDescription());
        assertEquals("https://example.com", foundProfile.getWebsite());
        assertEquals(Date.valueOf("1990-01-01"), foundProfile.getBirthday());
        assertNotNull(foundProfile.getCreationDate());
    }

    @Test
    public void testFindByUserUserId_WhenProfileDoesNotExist() {
        Profile foundProfile = profileRepository.findByUserUserId(1);

        assertEquals(null, foundProfile);
    }
}
