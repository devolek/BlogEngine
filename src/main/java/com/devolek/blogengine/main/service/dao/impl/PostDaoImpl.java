package com.devolek.blogengine.main.service.dao.impl;

import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.repo.PostRepository;
import com.devolek.blogengine.main.service.dao.PostDao;
import org.springframework.stereotype.Service;

@Service
public class PostDaoImpl implements PostDao {
    private final PostRepository postRepository;

    public PostDaoImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post findPostById(int id) {
        return null;
    }

    @Override
    public int getPostCountWithTag(Tag tag) {
        return 0;
    }

    @Override
    public int countAvailablePosts() {
        return 0;
    }

    @Override
    public int getCountModeration(ModerationStatus status, Integer moderatorId) {
        return postRepository.getCountModeration(status, moderatorId);
    }
}
