package com.example.socializingapp.dto.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreatePostDto {
    @NotBlank(message = "Content shouldn't be blank!")
    @Size(min = 10, message = "Content should have at least 10 characters!")
    String content;
}
