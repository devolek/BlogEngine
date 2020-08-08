package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.comments.request.AddCommentRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileWithPhotoRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileWithoutPhotoRequest;
import com.devolek.blogengine.main.dto.universal.InfoResponse;
import com.devolek.blogengine.main.security.UserDetailsImpl;
import com.devolek.blogengine.main.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class ApiGeneralController {
    private final ImageService imageService;
    private final CommentService commentService;
    private final UserService userService;
    private final TagService tagService;
    private final PostService postService;
    private final GlobalService globalService;

    public ApiGeneralController(ImageService imageService,
                                CommentService commentService,
                                UserService userService, TagService tagService, PostService postService, GlobalService globalService) {
        this.imageService = imageService;
        this.commentService = commentService;
        this.userService = userService;
        this.tagService = tagService;
        this.postService = postService;
        this.globalService = globalService;
    }

    @GetMapping("/api/init")
    public ResponseEntity<?> getInit() {
        return ResponseEntity.ok(new InfoResponse());
    }

    @PostMapping("/api/image")
    public ResponseEntity<?> saveImage(MultipartFile image) throws IOException {
        return ResponseEntity.ok(imageService.saveImage(image, 300));
    }

    @PostMapping("/api/comment")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetailsImpl user,
                                        @RequestBody AddCommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(user.getEmail(), request));
    }


    @GetMapping("/api/settings")
    public ResponseEntity<?> getSettings() {
        return ResponseEntity.ok(globalService.getSettings());
    }

    @PutMapping("/api/settings")
    public ResponseEntity<?> saveSettings(@RequestBody Map<String, Object> model) {
        return ResponseEntity.ok(globalService.setSettings(model));
    }

    @GetMapping("/api/tag")
    public ResponseEntity<?> getTags(String query) {
        return ResponseEntity.ok(tagService.getTagList(query));
    }

    @GetMapping("/api/calendar")
    public ResponseEntity<?> getCalendar(Integer year) {
        return ResponseEntity.ok(postService.getCalendar(year));
    }

    @PostMapping(path = "/api/profile/my", consumes = "application/json")
    public ResponseEntity<?> editProfile(@AuthenticationPrincipal UserDetailsImpl user,
                                         @RequestBody EditProfileWithoutPhotoRequest request) throws IOException {
        return ResponseEntity.ok(userService.editProfile(user.getId(), request));
    }

    @PostMapping(path = "/api/profile/my", consumes = "multipart/form-data")
    public ResponseEntity<?> editProfileWithPhoto(@AuthenticationPrincipal UserDetailsImpl user,
                                                  EditProfileWithPhotoRequest request) throws IOException {
        return ResponseEntity.ok(userService.editProfile(user.getId(), request));
    }

    @GetMapping("/api/statistics/my")
    public ResponseEntity<?> getMyStatistic(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(userService.getMyStatistic(user.getId()));
    }
}
