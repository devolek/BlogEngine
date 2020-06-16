package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.post.request.PostListRequest;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;

public interface PostService {
    CollectionResponse getPosts(PostListRequest request);
}
