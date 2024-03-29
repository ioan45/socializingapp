package com.example.socializingapp.dto.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetPostsResult {
    private List<PostDto> posts;
    private int currentPage;
    private int totalPages;
}
