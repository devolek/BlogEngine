package com.devolek.blogengine.main.dto.post.response;

import com.devolek.blogengine.main.dto.universal.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class PostCalendarResponse implements Response {
    private Set<Integer> years;
    private Map<String, Integer> posts;
}
