package com.example.socializingapp.dto.friends;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FriendDto {
    @NotBlank(message = "Username shouldn't be blank!")
    @Size(min = 2, max = 30, message = "Username should have 2-30 characters!")
    private String username;


    private Boolean bold;
}
