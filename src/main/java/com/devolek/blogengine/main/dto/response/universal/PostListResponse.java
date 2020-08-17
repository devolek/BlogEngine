package com.devolek.blogengine.main.dto.response.universal;

import com.devolek.blogengine.main.dto.response.post.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PostListResponse implements Response {
    private int count;
    private List<PostResponseDto> posts;
}
