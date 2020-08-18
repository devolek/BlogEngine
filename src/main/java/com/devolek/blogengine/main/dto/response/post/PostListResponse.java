package com.devolek.blogengine.main.dto.response.post;

import com.devolek.blogengine.main.dto.response.post.PostResponseDto;
import com.devolek.blogengine.main.dto.response.universal.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PostListResponse implements Response {
    private int count;
    private List<PostResponseDto> posts;
}
