package com.devolek.blogengine.main.dto.tag;

import com.devolek.blogengine.main.dto.tag.response.TagListResponse;
import com.devolek.blogengine.main.dto.tag.response.TagResponse;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.dto.universal.Dto;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagResponseFactory {
    public static Response getTagResponseList(List<Dto> tags) {
        return new TagListResponse(tags);
    }

    public List<Dto> getTagDtoList() {
        return new ArrayList<>();
    }

    public static Dto tagToDto(Tag tag, double weight) {
        return new TagResponse(tag.getName(), weight);
    }

}
