package com.devolek.blogengine.main.dto.response.tag;

import com.devolek.blogengine.main.dto.response.universal.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TagListResponse implements Response {
    private List<TagDto> tags;
}
