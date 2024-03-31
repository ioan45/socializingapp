package com.example.socializingapp.dto.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LikeDto {
    @Positive
    private int postId;

    @NotBlank(message = "Like status shouldn't be blank!")
    private String likeStatus;
}
