package com.example.socializingapp.repositories;

import com.example.socializingapp.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByUserUserId(Integer userId);
}
