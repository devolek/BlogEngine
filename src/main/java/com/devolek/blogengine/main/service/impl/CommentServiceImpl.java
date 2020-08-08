package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.comments.request.AddCommentRequest;
import com.devolek.blogengine.main.dto.comments.response.AddCommentResponse;
import com.devolek.blogengine.main.dto.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostComment;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.CommentRepository;
import com.devolek.blogengine.main.service.CommentService;
import com.devolek.blogengine.main.service.PostService;
import com.devolek.blogengine.main.service.dao.UserDao;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserDao userDao;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, UserDao userDao) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userDao = userDao;
    }

    @Override
    public PostComment findById(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Response addComment(String userEmail, AddCommentRequest request) {
        Response error = checkCommentRequest(request);
        if (error != null) {
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
        } else if (request.getText().length() < 10) {
            errors.put("text", "Текст комментария слишком короткий");
        }

        if (postService.findPostById(request.getPostId()) == null) {
            errors.put("post", "Такого поста не существует");
        }

        if (request.getParentId() != null && findById(request.getParentId()) == null) {
            errors.put("parent", "Такого комментария не существет");
        }
        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }
        return null;
    }
}
