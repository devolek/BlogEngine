package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.response.universal.Response;

public interface TagService {
    Response getTagList(String query);
}
