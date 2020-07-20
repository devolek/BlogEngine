package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.comments.request.AddCommentRequest;
import com.devolek.blogengine.main.dto.universal.InfoResponse;
import com.devolek.blogengine.main.model.GlobalSetting;
import com.devolek.blogengine.main.repo.GlobalSettingRepository;
import com.devolek.blogengine.main.security.UserDetailsImpl;
import com.devolek.blogengine.main.service.CommentService;
import com.devolek.blogengine.main.service.ImageService;
import com.devolek.blogengine.main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class ApiGeneralController {
    private final ImageService imageService;
    private final CommentService commentService;
    private final GlobalSettingRepository globalSettingRepository;
    private final TagService tagService;

    public ApiGeneralController(ImageService imageService,
                                CommentService commentService,
                                GlobalSettingRepository globalSettingRepository, TagService tagService) {
        this.imageService = imageService;
        this.commentService = commentService;
        this.globalSettingRepository = globalSettingRepository;
        this.tagService = tagService;
    }

    @GetMapping("/api/init")
    public ResponseEntity<?> getInit() {
        return ResponseEntity.ok(new InfoResponse());
    }

    @PostMapping("/api/image")
    public ResponseEntity<?> saveImage(MultipartFile image) throws IOException {
        return ResponseEntity.ok(imageService.saveImage(image));
    }

    @PostMapping("/api/comment")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetailsImpl user,
                                        @RequestBody AddCommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(user.getEmail(), request));
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
    public ResponseEntity<?> getTags(String query) {
        return ResponseEntity.ok(tagService.getTagList(query));
    }

}
