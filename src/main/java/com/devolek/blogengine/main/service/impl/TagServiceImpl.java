package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.repo.TagRepository;
import com.devolek.blogengine.main.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getTagsByList(List<String> tags) {
        List<Tag> tagList = new ArrayList<>();
        if (tags.size() != 0) {            //если тегов нет в запросе, блок пропускается
            tags.forEach(tag -> {
                Tag postTag;
                if (tagRepository.existsByNameIgnoreCase(tag)) {
                    postTag = tagRepository.findFirstByNameIgnoreCase(tag);
                } else {
                    postTag = new Tag();
                    postTag.setName(tag);
                }
                tagList.add(postTag);
            });
        }
        return tagList;
    }
}
