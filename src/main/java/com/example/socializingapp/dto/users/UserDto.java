package com.example.socializingapp.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    @NotBlank(message = "Username shouldn't be blank!")
    @Size(min = 2, max = 30, message = "Username should have 2-30 characters!")
    private String username;

    @NotBlank(message = "Email shouldn't be blank!")
    @Email(message = "Invalid email address!")
    private String email;

    @NotBlank(message = "Password shouldn't be blank!")
    @Size(min = 6, max = 40, message = "Password should have 6-40 characters!")
    private String password;
}
