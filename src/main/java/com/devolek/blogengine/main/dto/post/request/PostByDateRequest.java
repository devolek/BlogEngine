package com.devolek.blogengine.main.dto.post.request;

import lombok.Data;

@Data
public class PostByDateRequest {
    private int offset;
    private int limit;
    private String date;
}
