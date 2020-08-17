package com.devolek.blogengine.main.dto.request.post;

import com.devolek.blogengine.main.dto.response.universal.Response;
import lombok.Data;

@Data
public class SearchPostRequest implements Response {
    private int offset;
    private int limit;
    private String query;
}
