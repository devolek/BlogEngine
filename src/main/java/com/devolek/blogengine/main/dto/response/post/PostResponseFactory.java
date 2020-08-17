package com.devolek.blogengine.main.dto.response.post;

import com.devolek.blogengine.main.dto.response.comments.CommentDto;
import com.devolek.blogengine.main.dto.response.universal.PostListResponse;
import com.devolek.blogengine.main.dto.response.user.UserResponseFactory;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostComment;
import com.devolek.blogengine.main.model.Tag;
import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponseFactory {

    public static PostFullResponse getSinglePost(Post post) {
        return postToFullDto(post);
    }

    public static PostListResponse getPostsList(List<Post> posts, int count) {
        return new PostListResponse(count,
                posts.stream()
                        .map(PostResponseFactory::postToDto)
                        .collect(Collectors.toList()));
    }


    private static PostResponseDto postToDto(Post post) {

        int postLikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
        int postDislikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 0).count();

        return new PostResponseDto(post.getId(), post.getTime(),
                UserResponseFactory.getUserDto(post.getUser(), 0),
                post.getTitle(), Jsoup.parse(post.getText()).text().substring(0, 50), postLikes,
                postDislikes, post.getComments().size(), post.getViewCount());
    }

    private static PostFullResponse postToFullDto(Post post) {

        int postLikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
        int postDislikes = (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == -1).count();

        List<String> tags = post.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        List<CommentDto> comments = post.getComments()
                .stream()
                .map(PostResponseFactory::commentToDto)
                .collect(Collectors.toList());

        return new PostFullResponse(post.getId(), post.getTime(),
                UserResponseFactory.getUserDto(post.getUser(), 0),
                post.getTitle(), Jsoup.parse(post.getText()).text().substring(0, 50), postLikes,
                postDislikes, post.getComments().size(), post.getViewCount(), post.getText(), comments, tags);
    }

    private static CommentDto commentToDto(PostComment postComment) {
        return new CommentDto(postComment.getId(), postComment.getTime(),
                postComment.getText(), UserResponseFactory.getUserDto(postComment.getUser(), 0));
    }
}
