package com.devolek.blogengine.main.dto.response.post;

import com.devolek.blogengine.main.dto.response.comments.CommentDto;
import com.devolek.blogengine.main.dto.response.user.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostFullResponse extends PostResponseDto {
    private String text;
    private List<CommentDto> comments;
    private List<String> tags;

    public PostFullResponse(int id, Calendar time, UserDto user, String title,
                            String announce, int likeCount, int dislikeCount,
                            int commentCount, int viewCount, String text, List<CommentDto> comments,
                            List<String> tags) {
        super(id, time, user, title, announce, likeCount, dislikeCount, commentCount, viewCount);
        this.text = text;
        this.comments = comments;
        this.tags = tags;
    }
}
