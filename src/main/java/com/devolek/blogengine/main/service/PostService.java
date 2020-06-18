package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.post.request.*;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.dto.universal.Response;

import java.text.ParseException;

public interface PostService {
    CollectionResponse getPosts(PostListRequest request);

    CollectionResponse searchPosts(SearchPostRequest request);

    Response getPostById(int id);

    CollectionResponse getPostsByDate(PostByDateRequest request) throws ParseException;

    CollectionResponse getPostsByTag(PostByTagRequest request);

    CollectionResponse getPostsModeration(PostModerationRequest request, int moderatorId);

    CollectionResponse getMyPosts(PostModerationRequest request, int userId);

    Response addPost(PostAddRequest request, int userId) throws ParseException;

    Response likePost(int userId, int postId, int value);

    Response editPost(int userId, int postId, PostAddRequest request) throws ParseException;
}
