package com.devolek.blogengine.main.dto.response.tag;

import com.devolek.blogengine.main.model.Tag;

import java.util.List;

public class TagResponseFactory {
    public static TagListResponse getTagResponseList(List<TagDto> tags) {
        return new TagListResponse(tags);
    }

    public static TagDto tagToDto(Tag tag, double weight) {
        return new TagDto(tag.getName(), weight);
    }

}
