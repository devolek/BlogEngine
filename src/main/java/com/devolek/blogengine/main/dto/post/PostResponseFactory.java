package com.devolek.blogengine.main.dto.post;

import com.devolek.blogengine.main.dto.comments.response.CommentDto;
import com.devolek.blogengine.main.dto.post.response.PostResponseDto;
import com.devolek.blogengine.main.dto.post.response.PostFullResponse;
import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.dto.universal.Dto;
import com.devolek.blogengine.main.dto.user.UserResponseFactory;
import com.devolek.blogengine.main.dto.user.response.UserDto;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostComment;
import com.devolek.blogengine.main.model.Tag;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponseFactory {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
    public static PostFullResponse getSinglePost(Post post) {
        return postToFullDto(post);
    }

    public static CollectionResponse getPostsList(List<Post> posts, int count) {
        return new CollectionResponse(count,
                posts.stream()
                        .map(PostResponseFactory::postToDto)
                        .collect(Collectors.toList()));
    }


    private static PostResponseDto postToDto(Post post) {

        int postLikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
        int postDislikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == -1).count();

        return new PostResponseDto(post.getId(), post.getTime(),
                new UserDto(post.getUser().getId(), post.getUser().getName()),
                post.getTitle(), post.getText().substring(0, 50), postLikes,
                postDislikes, post.getComments().size(), post.getViewCount());
    }

    private static PostFullResponse postToFullDto(Post post) {

        int postLikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
        int postDislikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == -1).count();

        List<String> tags = post.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        List<Dto> comments = post.getComments()
                .stream()
                .map(PostResponseFactory::commentToDto)
                .collect(Collectors.toList());

        return new PostFullResponse(post.getId(), post.getTime(),
                new UserDto(post.getUser().getId(), post.getUser().getName()),
                post.getTitle(), post.getText().substring(0, 50), postLikes,
                postDislikes, post.getComments().size(), post.getViewCount(), post.getText(), comments, tags);
    }

    private static CommentDto commentToDto(PostComment postComment) {
        return new CommentDto(postComment.getId(), simpleDateFormat.format(postComment.getTime().getTime()),
                postComment.getText(), UserResponseFactory.getUserWithPhoto(postComment.getUser()));
    }
}
