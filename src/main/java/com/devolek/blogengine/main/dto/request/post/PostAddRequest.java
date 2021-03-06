package com.devolek.blogengine.main.dto.request.post;

import lombok.Data;

import java.util.List;

@Data
public class PostAddRequest {
    private long timestamp;
    private int active;
    private String title;
    private String text;
    private List<String> tags;
}
