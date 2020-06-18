package com.devolek.blogengine.main.dto.post.response;

import com.devolek.blogengine.main.dto.universal.Dto;
import com.devolek.blogengine.main.dto.universal.Response;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class PostFullResponse extends PostResponseDto implements Response {
    private String text;
    private List<Dto> comments;
    private List<String> tags;

    public PostFullResponse(int id, Calendar time, Dto user, String title,
                            String announce, int likeCount, int dislikeCount,
                            int commentCount, int viewCount, String text, List<Dto> comments,
                            List<String> tags) {
        super(id, time, user, title, announce, likeCount, dislikeCount, commentCount, viewCount);
        this.text = text;
        this.comments = comments;
        this.tags = tags;
    }
}
