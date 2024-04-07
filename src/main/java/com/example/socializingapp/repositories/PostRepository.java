package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Post;
import com.example.socializingapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT ps " +
            "FROM Post ps, Friendship fs " +
            "WHERE (fs.sender.username = ps.user.username or fs.receiver.username = ps.user.username) and " +
            "       ps.user.username != ?1 and " +
            "      (fs.sender.username = ?1 or fs.receiver.username = ?1)")
    Page<Post> getFriendsPosts(String forUsername, Pageable pageable);

    @Query("SELECT ps " +
            "FROM Post ps " +
            "WHERE ps.user.username = ?1 ")
    Page<Post> getMyPosts(String forUsername, Pageable pageable);
}
