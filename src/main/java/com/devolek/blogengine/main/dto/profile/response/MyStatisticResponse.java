package com.devolek.blogengine.main.dto.profile.response;

import com.devolek.blogengine.main.dto.universal.Response;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;


@Data
@AllArgsConstructor
public class MyStatisticResponse implements Response {
    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Europe/Moscow")
    private Calendar firstPublication;
}
