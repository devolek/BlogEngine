package com.devolek.blogengine.main.dto.post.response;

import com.devolek.blogengine.main.dto.universal.Dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Calendar;

@Data
public class PostResponseDto implements Dto {
    private int id;
    @JsonFormat(pattern = "MMM d, HH:mm", timezone = "Europe/Moscow")
    private Calendar time;
    private Dto user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public PostResponseDto(int id, Calendar time, Dto user, String title,
                           String announce, int likeCount, int dislikeCount,
                           int commentCount, int viewCount) {
        this.id = id;
        this.time = time;
        this.user = user;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }
}
