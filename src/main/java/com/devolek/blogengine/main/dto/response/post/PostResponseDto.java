package com.devolek.blogengine.main.dto.response.post;

import com.devolek.blogengine.main.dto.response.View;
import com.devolek.blogengine.main.dto.response.user.UserDto;
import com.devolek.blogengine.main.util.CalendarToSecondsSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;

@Data
@AllArgsConstructor
public class PostResponseDto {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonSerialize(using = CalendarToSecondsSerializer.class)
    private Calendar timestamp;
    private UserDto user;
    private String title;

    @JsonView(View.POST_LIST.class)
    private String announce;

    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public PostResponseDto() {
    }
}
