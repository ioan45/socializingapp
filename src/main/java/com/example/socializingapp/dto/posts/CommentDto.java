package com.example.socializingapp.dto.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentDto {
    // The Dto is used for both the requests and responses.
    // The fields used for requests are annotated.

    @Positive
    private int postId;

    private String username;

    @NotBlank(message = "Content shouldn't be blank!")
    private String content;

    private String creationDate;
}
