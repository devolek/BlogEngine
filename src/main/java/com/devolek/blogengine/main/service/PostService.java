package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.request.post.*;
import com.devolek.blogengine.main.dto.response.post.PostCalendarResponse;
import com.devolek.blogengine.main.dto.response.post.PostFullResponse;
import com.devolek.blogengine.main.dto.response.profile.StatisticResponse;
import com.devolek.blogengine.main.dto.response.post.PostListResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;

import java.text.ParseException;
import java.util.List;

public interface PostService {
    Post findPostById(int id);

    PostListResponse getPosts(PostListRequest request);

    PostListResponse searchPosts(SearchPostRequest request);

    PostFullResponse getPostById(int id);

    PostListResponse getPostsByDate(int offset, int limit, String date) throws ParseException;

    PostListResponse getPostsByTag(PostByTagRequest request);

    PostListResponse getPostsModeration(PostModerationRequest request, int moderatorId);

    PostListResponse getMyPosts(PostModerationRequest request, int userId);

    Response addPost(PostAddRequest request, int userId) throws ParseException;

    Response likePost(int userId, int postId, int value);

    Response editPost(int userId, int postId, PostAddRequest request) throws ParseException;

    Response addPostDecision(AddModerationRequest request, int userId);

    PostCalendarResponse getCalendar(Integer year);

    int getPostCountWithTag(Tag tag);

    int countAvailablePosts();

    List<Tag> getTagsByList(List<String> tags);

    StatisticResponse getStatistic(Integer userId);
}
