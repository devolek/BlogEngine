package com.devolek.blogengine.main.dto.post.response;

import com.devolek.blogengine.main.dto.universal.CollectionResponse;
import com.devolek.blogengine.main.dto.universal.OkResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.dto.user.response.UserDto;
import com.devolek.blogengine.main.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostResponseFactory {
    public static Response getSinglePost(Post post) {
        return new OkResponse();
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
}
