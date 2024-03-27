package com.example.socializingapp.services;

import com.example.socializingapp.dto.posts.CommentDto;
import com.example.socializingapp.dto.posts.CreatePostDto;
import com.example.socializingapp.dto.posts.LikeDto;
import com.example.socializingapp.dto.posts.PostDto;
import com.example.socializingapp.entities.Comment;
import com.example.socializingapp.entities.Post;
import com.example.socializingapp.entities.User;
import com.example.socializingapp.repositories.CommentRepository;
import com.example.socializingapp.repositories.PostRepository;
import com.example.socializingapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PostsService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    public PostsService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<PostDto> getPosts(boolean friendsPosts) {
        List<PostDto> result = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<Post> posts = (friendsPosts ? postRepository.getFriendsPosts(username) : postRepository.getMyPosts(username));
        for(Post post : posts) {
            boolean isLiked = post.getLikes().contains(user);
            String date = dateFormat.format(post.getTimestamp());

            List<Comment> comments = commentRepository.findAllByPostOrderByTimestampAsc(post);
            List<CommentDto> commentDtos = new ArrayList<>();
            for (Comment comment : comments) {
                String commentDate = dateFormat.format(comment.getTimestamp());
                commentDtos.add(new CommentDto(0, comment.getUser().getUsername(), comment.getContent(), commentDate));
            }

            result.add(new PostDto(post.getPostId(), post.getUser().getUsername(), post.getContent(), post.getLikesCount(), isLiked, date, commentDtos));
        }
        return result;
    }

    public void toggleLike(LikeDto like) {

        Post post = postRepository.findById(like.getPostId()).orElse(null);
        if (post != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (like.getLikeStatus().equals("liked")) {
                post.setLikesCount(post.getLikesCount() + 1);
                userRepository.findByUsername(username).ifPresent(user -> post.addLike(user));
            }
            else {
                post.setLikesCount(post.getLikesCount() - 1);
                userRepository.findByUsername(username).ifPresent(user -> post.removeLike(user));
            }
            postRepository.save(post);
        }
    }

    public void postComment(CommentDto comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        Post post = postRepository.findById(comment.getPostId()).orElse(null);
        if (user != null && post != null) {
            Timestamp commentDate = new Timestamp(Long.parseLong(comment.getCreationDate()));
            Comment newComment = new Comment(0, post, user, comment.getContent(), commentDate);
            commentRepository.save(newComment);
        }
    }

    @Transactional
    public void deletePost(Integer postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || !post.getUser().getUsername().equals(username))
            return;
        post.removeAllLikes();
        postRepository.save(post);
        commentRepository.deleteByPost(post);
        postRepository.delete(post);
    }

    public void createPost(CreatePostDto payload) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            return;
        Post newPost = new Post();
        newPost.setUser(user);
        newPost.setContent(payload.getContent());
        newPost.setTimestamp(new Timestamp(payload.getCreationTime()));
        postRepository.save(newPost);
    }
}
