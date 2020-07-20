package com.devolek.blogengine.main.dto.tag.response;

import com.devolek.blogengine.main.dto.universal.Dto;
import com.devolek.blogengine.main.dto.universal.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TagListResponse implements Response {
    private List<Dto> tags;
}
