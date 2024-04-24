package com.example.socializingapp.dto.posts;

import com.example.socializingapp.entities.Comment;
import com.example.socializingapp.entities.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PostsDtoMapper {

    public PostDto fromPost(Post post, boolean isLikedByAuthUser) {
        return new PostDto(
                post.getPostId(),
                post.getUser().getUsername(),
                post.getContent(),
                post.getLikesCount(),
                isLikedByAuthUser,
                (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(post.getTimestamp()),
                fromCommentList(post.getComments())
        );
    }

    public CommentDto fromComment(Comment comment) {
        return new CommentDto(
                comment.getPost().getPostId(),
                comment.getUser().getUsername(),
                comment.getContent(),
                (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(comment.getTimestamp())
        );
    }

    public List<CommentDto> fromCommentList(List<Comment> list) {
        List<CommentDto> result = new ArrayList<>();
        list.forEach(comment -> result.add(fromComment(comment)));
        return result;
    }
}
