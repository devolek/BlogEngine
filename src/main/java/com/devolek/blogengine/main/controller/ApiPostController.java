package com.devolek.blogengine.main.controller;


import com.devolek.blogengine.main.dto.post.request.*;
import com.devolek.blogengine.main.dto.universal.OkResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.security.UserDetailsImpl;
import com.devolek.blogengine.main.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {

        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<?> getPosts(PostListRequest request) {
        return ResponseEntity.ok(postService.getPosts(request));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPost(@RequestBody SearchPostRequest request) {
        return ResponseEntity.ok(postService.searchPosts(request));
    }

    @GetMapping("/{ID}")
    public ResponseEntity<?> getPost(@PathVariable int ID) {
        Response response = postService.getPostById(ID);
        return response instanceof OkResponse ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(response) :
                ResponseEntity.ok(response);
    }

    @GetMapping("/byDate")
    public ResponseEntity<?> getPostByDate(@RequestBody PostByDateRequest request) throws ParseException {
        return ResponseEntity.ok(postService.getPostsByDate(request));
    }

    @GetMapping("/byTag")
    public ResponseEntity<?> getPostByTag(PostByTagRequest request) {
        return ResponseEntity.ok(postService.getPostsByTag(request));
    }

    @GetMapping("/moderation")
    public ResponseEntity<?> getPostModeration(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               PostModerationRequest request) {
        return ResponseEntity.ok(postService.getPostsModeration(request, userDetails.getId()));
    }

    @PostMapping("/moderation")
    public ResponseEntity<?> addPostModeration(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               AddModerationRequest request) {
        return ResponseEntity.ok(postService.addPostDecision(request, userDetails.getId()));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        PostModerationRequest request) {
        return ResponseEntity.ok(postService.getMyPosts(request, userDetails.getId()));
    }

    @PostMapping
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestBody PostAddRequest request) throws ParseException {
        return ResponseEntity.ok(postService.addPost(request, userDetails.getId()));
    }

    @PostMapping("/like")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody LikeRequest request) {
        return ResponseEntity.ok(postService.likePost(userDetails.getId(), request.getPostId(), 1));
    }

    @PostMapping("/dislike")
    public ResponseEntity<?> dislikePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody LikeRequest request) {
        return ResponseEntity.ok(postService.likePost(userDetails.getId(), request.getPostId(), -1));
    }

    @PutMapping("/{ID}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody PostAddRequest request,
                                      @PathVariable int ID) throws ParseException {
        return ResponseEntity.ok(postService.editPost(userDetails.getId(), ID, request));
    }
}
