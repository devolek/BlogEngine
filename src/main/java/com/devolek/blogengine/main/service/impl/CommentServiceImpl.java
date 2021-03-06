package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.request.comments.AddCommentRequest;
import com.devolek.blogengine.main.dto.response.comments.AddCommentResponse;
import com.devolek.blogengine.main.dto.response.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.universal.UniversalResponseFactory;
import com.devolek.blogengine.main.exeption.InvalidRequestException;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostComment;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.CommentRepository;
import com.devolek.blogengine.main.service.CommentService;
import com.devolek.blogengine.main.service.PostService;
import com.devolek.blogengine.main.service.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserDao userDao;
    @Value("${post.comment.minLength}")
    private int commentMinLength;


    @Override
    public PostComment findById(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Response addComment(String userEmail, AddCommentRequest request) {
        Response error = checkCommentRequest(request);
        if (error instanceof ErrorResponse) {
            return error;
        }
        PostComment comment = new PostComment();
        Post post = postService.findPostById(request.getPostId());
        PostComment parentComment = request.getParentId() == null ? null : findById(request.getParentId());
        User commentAuthor = userDao.findByEmail(userEmail);
        comment.setPost(post);
        comment.setTime(Calendar.getInstance());
        comment.setText(request.getText());
        if (parentComment != null) {
            comment.setParent(parentComment);
        }
        comment.setUser(commentAuthor);
        return new AddCommentResponse(commentRepository.save(comment).getId());
    }

    @Override
    public Response checkCommentRequest(AddCommentRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        if (request.getText().isEmpty()) {
            errors.put("text", "Текст комментария не установлен");
        } else if (request.getText().length() < commentMinLength) {
            errors.put("text", "Текст комментария слишком короткий, введите не менее " + commentMinLength + "-ти символов.");
        }

        if (postService.findPostById(request.getPostId()) == null) {
            throw new InvalidRequestException("invalid post id");
        }

        if (request.getParentId() != null && findById(request.getParentId()) == null) {
            throw new InvalidRequestException("invalid comment id");
        }
        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }
        return UniversalResponseFactory.getTrueResponse();
    }
}
