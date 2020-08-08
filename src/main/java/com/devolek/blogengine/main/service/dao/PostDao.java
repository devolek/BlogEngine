package com.devolek.blogengine.main.service.dao;

import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;

public interface PostDao {
    Post findPostById(int id);

    int getPostCountWithTag(Tag tag);

    int countAvailablePosts();

    int getCountModeration(ModerationStatus status, Integer moderatorId);
}
