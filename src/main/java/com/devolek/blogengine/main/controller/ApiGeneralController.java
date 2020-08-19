package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.request.comments.AddCommentRequest;
import com.devolek.blogengine.main.dto.request.profile.EditProfileWithPhotoRequest;
import com.devolek.blogengine.main.dto.request.profile.EditProfileWithoutPhotoRequest;
import com.devolek.blogengine.main.dto.response.universal.InfoResponse;
import com.devolek.blogengine.main.security.jwt.UserDetailsImpl;
import com.devolek.blogengine.main.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@RestController
public class ApiGeneralController {
    private final ImageService imageService;
    private final CommentService commentService;
    private final UserService userService;
    private final TagService tagService;
    private final PostService postService;
    private final GlobalService globalService;

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

    @Secured("ROLE_MODERATOR")
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
    public ResponseEntity<?> editProfile(HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl user,
                                         @RequestBody EditProfileWithoutPhotoRequest request) throws IOException {
        return ResponseEntity.ok(userService.editProfile(user.getId(), request, response));
    }

    @PostMapping(path = "/api/profile/my", consumes = "multipart/form-data")
    public ResponseEntity<?> editProfileWithPhoto(HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl user,
                                                  EditProfileWithPhotoRequest request) throws IOException {
        return ResponseEntity.ok(userService.editProfile(user.getId(), request, response));
    }

    @GetMapping("/api/statistics/my")
    public ResponseEntity<?> getMyStatistic(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(userService.getMyStatistic(user.getId()));
    }
}
