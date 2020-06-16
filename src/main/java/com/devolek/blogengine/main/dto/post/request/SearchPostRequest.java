package com.devolek.blogengine.main.dto.post.request;

import com.devolek.blogengine.main.dto.universal.Response;
import lombok.Data;

@Data
public class SearchPostRequest implements Response {
    private int offset;
    private int limit;
    private String query;
}
