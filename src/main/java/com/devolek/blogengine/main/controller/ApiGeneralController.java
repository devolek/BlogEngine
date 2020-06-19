package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.universal.InfoResponse;
import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.GlobalSetting;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.repo.GlobalSettingRepository;
import com.devolek.blogengine.main.repo.PostRepository;
import com.devolek.blogengine.main.repo.TagRepository;
import com.devolek.blogengine.main.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class ApiGeneralController {
    private final ImageService imageService;
    @Autowired
    private GlobalSettingRepository globalSettingRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    public ApiGeneralController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/api/init")
    public ResponseEntity<?> getInit() {
        return ResponseEntity.ok(new InfoResponse());
    }

    @PostMapping("/api/image")
    public ResponseEntity<?> saveImage(MultipartFile image) throws IOException {
        return ResponseEntity.ok(imageService.saveImage(image));
    }

    @GetMapping("/api/settings")
    public Map<String, Object> getSettings() {
        HashMap<String, Object> model = new HashMap<>();
        Iterable<GlobalSetting> iterable = globalSettingRepository.findAll();
        for (GlobalSetting globalSetting : iterable) {
            model.put(globalSetting.getCode(), globalSetting.getValue().equals("YES"));
        }
        return model;
    }

    @PutMapping("/api/settings")
    public void saveSettings(@RequestBody Map<String, Object> model) {
        for (String code : model.keySet()) {
            Optional<GlobalSetting> setting = globalSettingRepository.findByCode(code);
            if (setting.isPresent()) {
                GlobalSetting globalSetting = setting.get();
                globalSetting.setValue(model.get(code).equals(true) ? "YES" : "NO");
                globalSettingRepository.save(globalSetting);
            }
        }
    }

    @GetMapping("/api/tag")
    public Map<String, Object> getTags(String query) {
        ArrayList<Map<String, Object>> tagList = new ArrayList<>();
        HashMap<String, Object> model = new HashMap<>();

        Iterable<Tag> tags;
        if (query == null || query.isEmpty()) {
            tags = tagRepository.findAll();
        } else {
            tags = tagRepository.findAllByNameStartsWith(query);
        }

        Iterable<Post> postIterable = postRepository.findAll();
        int postCount = 0;
        for (Post post : postIterable) {
            if (post.getIsActive() == 1 &&
                    post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    !post.getTime().after(Calendar.getInstance())) {
                postCount++;
            }
        }
        if (postCount == 0) {
            model.put("tags", tagList);
            return model;
        }
        double maxWeight = 0;

        for (Tag tag : tagRepository.findAll()) {
            int postWithTag = postRepository.findAllByTagsContains(tag).size();
            double tagWeight = (double) postWithTag / (double) postCount;
            maxWeight = Math.max(maxWeight, tagWeight);
        }

        for (Tag tag : tags) {
            int postWithTag = postRepository.findAllByTagsContains(tag).size();
            double tagWeight = ((double) postWithTag / (double) postCount) / maxWeight;
            HashMap<String, Object> tagWithWeight = new HashMap<>();
            tagWithWeight.put("name", tag.getName());
            tagWithWeight.put("weight", tagWeight);
            tagList.add(tagWithWeight);
        }
        model.put("tags", tagList);
        return model;
    }

}
