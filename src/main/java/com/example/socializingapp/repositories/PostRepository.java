package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Modifying
    @Query("SELECT ps " +
            "FROM Post ps, Friendship fs " +
            "WHERE (fs.sender.username = ?1 or fs.receiver.username = ?1) and " +
            "       ps.user.username != ?1 and " +
            "      (fs.sender.username = ps.user.username or fs.receiver.username = ps.user.username)")
    List<Post> getFriendsPosts(String forUsername);
}
