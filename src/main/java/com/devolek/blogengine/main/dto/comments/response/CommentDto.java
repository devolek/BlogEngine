package com.devolek.blogengine.main.dto.comments.response;

import com.devolek.blogengine.main.dto.universal.Dto;
import lombok.Data;

@Data
public class CommentDto implements Dto {
    private int id;
    private String time;
    private String text;
    private Dto user;

    public CommentDto(int id, String time, String text, Dto user) {
        this.id = id;
        this.time = time;
        this.text = text;
        this.user = user;
    }
}
