package com.example.socializingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String description;
    private String website;
    private Date birthday;
    private Timestamp creationDate;

    @Override
    public String toString() {
        return "Profile{" +
                "profileId=" + profileId +
                ", user=" + user.getUsername() +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", birthday=" + birthday +
                ", creationDate=" + creationDate +
                '}';
    }
}
