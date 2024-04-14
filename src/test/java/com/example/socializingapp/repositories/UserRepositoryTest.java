package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("postgre")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {

        // Arrange

        User user1 = new User();
        user1.setUserId(0);
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");

        // Act

        int resultedUserId = userRepository.save(user1).getUserId();

        // Assert

        assertNotEquals(0, resultedUserId);
    }

    @Test
    public void testFindByUsername() {

        // Arrange

        User user1 = new User();
        user1.setUserId(0);
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        userRepository.save(user1);

        // Act

        Optional<User> result = userRepository.findByUsername("ioan45");

        // Assert

        assertTrue(result.isPresent());
    }

    @Test
    public void testExistsByUsername() {

        // Arrange

        User user1 = new User();
        user1.setUserId(0);
        user1.setUsername("ioan45");
        user1.setPassword("testPass");
        user1.setEmail("ioan45@gmail.com");
        userRepository.save(user1);

        // Act

        boolean result = userRepository.existsByUsername("ioan45");

        // Assert

        assertTrue(result);
    }
}
