package com.example.socializingapp.dto.profiles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProfileDto {
    String username;
    String email;
    @NotBlank(message = "Description cannot be blank")
    String description;
    @NotBlank(message = "Website cannot be blank")
    String website;
    @PastOrPresent(message = "Birthday cannot be after current date")
    Date birthday;
    Timestamp creationDate;
}
