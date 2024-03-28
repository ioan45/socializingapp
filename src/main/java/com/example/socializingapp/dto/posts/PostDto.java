package com.example.socializingapp.dto.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostDto {
    private int postId;
    private String owner;
    private String content;
    private int likesCount;
    private boolean isLiked;
    private String creationDate;
    private List<CommentDto> comments;
}
