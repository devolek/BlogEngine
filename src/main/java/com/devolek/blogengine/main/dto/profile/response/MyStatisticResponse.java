package com.devolek.blogengine.main.dto.profile.response;

import com.devolek.blogengine.main.dto.universal.Response;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@Data
@AllArgsConstructor
public class MyStatisticResponse implements Response {
    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, timezone = "UTC")
    private Date firstPublication;
}
