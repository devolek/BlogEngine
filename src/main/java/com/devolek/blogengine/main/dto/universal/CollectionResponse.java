package com.devolek.blogengine.main.dto.universal;

import lombok.Data;

import java.util.List;

@Data
public class CollectionResponse implements Response{
    private int count;
    private List<Dto> posts;

    public CollectionResponse(int count, List<Dto> posts) {
        this.count = count;
        this.posts = posts;
    }
}
