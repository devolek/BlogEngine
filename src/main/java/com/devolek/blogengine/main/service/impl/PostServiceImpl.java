package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.post.request.PostListRequest;
import com.devolek.blogengine.main.dto.post.response.PostResponseFactory;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.repo.PostRepository;
import com.devolek.blogengine.main.service.PostService;
import com.devolek.blogengine.main.utils.OffsetBasedPageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public CollectionResponse getPosts(PostListRequest request) {
        int count = postRepository.getAvailablePostCount();
        int page = request.getOffset() / request.getLimit();
        List<Post> posts = new ArrayList<>();
        switch (request.getMode()) {
            case "recent": {
                posts = postRepository.getRecentPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            case "popular": {
                posts = postRepository.getPopularPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            case "best": {
                posts = postRepository.getBestPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            case "early": {
                posts = postRepository.getEarlyPosts(PageRequest.of(page, request.getLimit()));
                break;
            }
            default:
                break;
        }
        return PostResponseFactory.getPostsList(posts, count);
    }

}
