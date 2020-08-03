package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.post.request.*;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;

import java.text.ParseException;
import java.util.List;

public interface PostService {
    Post findPostById(int id);

    CollectionResponse getPosts(PostListRequest request);

    CollectionResponse searchPosts(SearchPostRequest request);

    Response getPostById(int id);

    CollectionResponse getPostsByDate(int offset, int limit, String date) throws ParseException;

    CollectionResponse getPostsByTag(PostByTagRequest request);

    CollectionResponse getPostsModeration(PostModerationRequest request, int moderatorId);

    CollectionResponse getMyPosts(PostModerationRequest request, int userId);

    Response addPost(PostAddRequest request, int userId) throws ParseException;

    Response likePost(int userId, int postId, int value);

    Response editPost(int userId, int postId, PostAddRequest request) throws ParseException;

    Response addPostDecision(AddModerationRequest request, int userId);

    Response getCalendar(Integer year);

    int getPostCountWithTag(Tag tag);

    int countAvailablePosts();

    List<Tag> getTagsByList(List<String> tags);
}
