package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.request.comments.AddCommentRequest;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.model.PostComment;

public interface CommentService {

    PostComment findById(int id);

    Response addComment(String userEmail, AddCommentRequest request);

    Response checkCommentRequest(AddCommentRequest request);
}
