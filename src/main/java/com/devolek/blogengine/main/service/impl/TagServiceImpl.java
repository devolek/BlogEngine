package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.response.tag.TagDto;
import com.devolek.blogengine.main.dto.response.tag.TagResponseFactory;
import com.devolek.blogengine.main.dto.response.universal.PostListResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.repo.TagRepository;
import com.devolek.blogengine.main.service.PostService;
import com.devolek.blogengine.main.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final PostService postService;

    @Override
    public Response getTagList(String query) {
        List<Tag> tags = tagRepository.getAllTagsWithQuery(query);
        if (tags == null) {
            return new PostListResponse(0, new ArrayList<>());
        }
        List<TagDto> tagList = new ArrayList<>();
        int allPost = postService.countAvailablePosts();
        double maxWeight = 0;
        for (Tag tag : tags) {
            int postCountWithTag = postService.getPostCountWithTag(tag);
            double tagWeight = ((double) postCountWithTag / (double) allPost);
            maxWeight = Math.max(maxWeight, tagWeight);
        }
        for (Tag tag : tags) {
            int postCountWithTag = postService.getPostCountWithTag(tag);
            double tagWeight = ((double) postCountWithTag / (double) allPost) / maxWeight;
            tagList.add(TagResponseFactory.tagToDto(tag, tagWeight));
        }

        return TagResponseFactory.getTagResponseList(tagList);
    }
}
