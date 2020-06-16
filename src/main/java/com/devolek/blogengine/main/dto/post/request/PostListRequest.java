package com.devolek.blogengine.main.dto.post.request;

import lombok.Data;

@Data
public class PostListRequest {
    private int offset;
    private int limit;
    private String mode;
}
