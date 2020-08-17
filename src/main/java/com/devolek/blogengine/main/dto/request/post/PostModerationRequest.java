package com.devolek.blogengine.main.dto.request.post;

import lombok.Data;

@Data
public class PostModerationRequest {
    private int offset;
    private int limit;
    private String status;
}
