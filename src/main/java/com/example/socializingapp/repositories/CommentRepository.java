package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Comment;
import com.example.socializingapp.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPost(Post post);
}
