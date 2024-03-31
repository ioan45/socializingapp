package com.example.socializingapp.dto.posts;

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
public class CreatePostDto {
    @NotBlank(message = "Content shouldn't be blank!")
    @Size(min = 10, message = "Content should have at least 10 characters!")
    String content;
}
