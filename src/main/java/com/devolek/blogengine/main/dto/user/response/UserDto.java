package com.devolek.blogengine.main.dto.user.response;

import com.devolek.blogengine.main.dto.universal.Dto;
import lombok.Data;

@Data
public class UserDto implements Dto {
    private int id;
    private String name;

    public UserDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
