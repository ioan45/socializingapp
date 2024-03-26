package com.example.socializingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToMany(mappedBy = "sender")
    private List<Friendship> friendshipsSent;

    @OneToMany(mappedBy = "receiver")
    private List<Friendship> friendshipsReceived;

    @OneToMany(mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver")
    private List<Message> messagesReceived;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentsMade;

    @OneToMany(mappedBy = "user")
    private List<Post> postsMade;

    @ManyToMany
    @JoinTable(name = "post_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> postsLiked;

    @Column(unique = true)
    private String username;
    private String email;
    private String password;
}
