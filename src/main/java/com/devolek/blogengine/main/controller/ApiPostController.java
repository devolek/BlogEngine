package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.request.post.*;
import com.devolek.blogengine.main.dto.response.View;
import com.devolek.blogengine.main.security.jwt.UserDetailsImpl;
import com.devolek.blogengine.main.service.PostService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;

    @GetMapping()
    @JsonView(View.POST_LIST.class)
    public ResponseEntity<?> getPosts(PostListRequest request) {
        return ResponseEntity.ok(postService.getPosts(request));
    }

    @JsonView(View.POST_LIST.class)
    @GetMapping("/search")
    public ResponseEntity<?> searchPost(SearchPostRequest request) {
        return ResponseEntity.ok(postService.searchPosts(request));
    }

    @JsonView(View.USER_WITH_PHOTO.class)
    @GetMapping("/{ID}")
    public ResponseEntity<?> getPost(@PathVariable int ID) {
        return ResponseEntity.ok(postService.getPostById(ID));
    }

    @JsonView(View.POST_LIST.class)
    @GetMapping("/byDate")
    public ResponseEntity<?> getPostByDate(int offset, int limit, String date) throws ParseException {
        return ResponseEntity.ok(postService.getPostsByDate(offset, limit, date));
    }

    @JsonView(View.POST_LIST.class)
    @GetMapping("/byTag")
    public ResponseEntity<?> getPostByTag(PostByTagRequest request) {
        return ResponseEntity.ok(postService.getPostsByTag(request));
    }

    @JsonView(View.POST_LIST.class)
    @Secured("ROLE_MODERATOR")
    @GetMapping("/moderation")
    public ResponseEntity<?> getPostModeration(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               PostModerationRequest request) {
        return ResponseEntity.ok(postService.getPostsModeration(request, userDetails.getId()));
    }

    @JsonView(View.POST_LIST.class)
    @Secured("ROLE_USER")
    @GetMapping("/my")
    public ResponseEntity<?> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        PostModerationRequest request) {
        return ResponseEntity.ok(postService.getMyPosts(request, userDetails.getId()));
    }

    @Secured("ROLE_USER")
    @PostMapping()
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestBody PostAddRequest request) throws ParseException {
        return ResponseEntity.ok(postService.addPost(request, userDetails.getId()));
    }

    @Secured("ROLE_USER")
    @PostMapping("/like")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody LikeRequest request) {
        return ResponseEntity.ok(postService.likePost(userDetails.getId(), request.getPostId(), 1));
    }

    @Secured("ROLE_USER")
    @PostMapping("/dislike")
    public ResponseEntity<?> dislikePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody LikeRequest request) {
        return ResponseEntity.ok(postService.likePost(userDetails.getId(), request.getPostId(), 0));
    }

    @Secured("ROLE_USER")
    @PutMapping("/{ID}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody PostAddRequest request,
                                      @PathVariable int ID) throws ParseException {
        return ResponseEntity.ok(postService.editPost(userDetails.getId(), ID, request));
    }
}
