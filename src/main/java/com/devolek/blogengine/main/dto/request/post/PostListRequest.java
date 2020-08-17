package com.devolek.blogengine.main.dto.request.post;

import lombok.Data;

@Data
public class PostListRequest {
    private int offset;
    private int limit;
    private String mode;
}
