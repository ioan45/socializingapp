package com.example.socializingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "postsLiked")
    List<User> likes;

    private String content;
    private Timestamp timestamp;
    private int likesCount;

    public void addLike(User user) {
        likes.add(user);
        user.getPostsLiked().add(this);
    }

    public void removeLike(User user) {
        likes.remove(user);
        user.getPostsLiked().remove(this);
    }

    public void removeAllLikes() {
        likes.forEach(user -> user.getPostsLiked().remove(this));
        likes.clear();
    }
}
