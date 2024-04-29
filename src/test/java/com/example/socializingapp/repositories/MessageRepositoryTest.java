package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Message;
import com.example.socializingapp.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("postgre")
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllByUsers_WhenMessagesExist() {
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        Message message1 = new Message();
        message1.setSender(user1);
        message1.setReceiver(user2);
        message1.setContent("Hello from user1 to user2");
        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setSender(user2);
        message2.setReceiver(user1);
        message2.setContent("Hello back from user2 to user1");
        messageRepository.save(message2);

        List<Message> messages = messageRepository.findAllByUsers("user1", "user2");

        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertEquals("Hello from user1 to user2", messages.get(0).getContent());
        assertEquals("Hello back from user2 to user1", messages.get(1).getContent());
    }

    @Test
    public void testFindAllByUsers_WhenNoMessagesExist() {
        List<Message> messages = messageRepository.findAllByUsers("user1", "user2");

        assertNotNull(messages);
        assertEquals(0, messages.size());
    }
}
