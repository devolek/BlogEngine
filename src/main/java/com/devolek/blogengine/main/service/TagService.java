package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.model.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getTagsByList(List<String> tags);
}
