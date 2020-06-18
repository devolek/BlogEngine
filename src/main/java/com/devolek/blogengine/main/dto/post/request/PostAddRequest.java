package com.devolek.blogengine.main.dto.post.request;

import lombok.Data;

import java.util.List;

@Data
public class PostAddRequest {
    private String time;
    private int active;
    private String title;
    private String text;
    private List<String> tags;
}
