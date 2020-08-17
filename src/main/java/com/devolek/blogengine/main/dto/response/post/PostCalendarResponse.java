package com.devolek.blogengine.main.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class PostCalendarResponse {
    private Set<Integer> years;
    private Map<String, Integer> posts;
}
