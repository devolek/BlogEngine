package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.universal.Response;

public interface TagService {
    Response getTagList(String query);
}
