package com.devolek.blogengine.main.dto.response.profile;

import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.util.CalendarToSecondsSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;


@Data
@AllArgsConstructor
public class StatisticResponse implements Response {
    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonSerialize(using = CalendarToSecondsSerializer.class)
    private Calendar firstPublication;
}
