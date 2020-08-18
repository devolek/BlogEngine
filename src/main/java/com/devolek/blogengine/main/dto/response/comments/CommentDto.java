package com.devolek.blogengine.main.dto.response.comments;

import com.devolek.blogengine.main.dto.response.user.UserDto;
import com.devolek.blogengine.main.util.CalendarToSecondsSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;

@AllArgsConstructor
@Data
public class CommentDto {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonSerialize(using = CalendarToSecondsSerializer.class)
    private Calendar timestamp;
    private String text;
    private UserDto user;
}
