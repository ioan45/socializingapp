package com.example.socializingapp.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePostDto {
    String content;
    Long creationTime;  // milliseconds since epoch
}
