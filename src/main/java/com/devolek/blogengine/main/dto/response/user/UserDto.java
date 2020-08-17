package com.devolek.blogengine.main.dto.response.user;

import com.devolek.blogengine.main.dto.response.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    @JsonView(View.USER_ID_NAME.class)
    private int id;

    @JsonView(View.USER_ID_NAME.class)
    private String name;

    @JsonView(View.USER_WITH_PHOTO.class)
    private String photo;

    @JsonView(View.USER_FULL_INFO.class)
    private String email;

    @JsonView(View.USER_FULL_INFO.class)
    private boolean moderation;

    @JsonView(View.USER_FULL_INFO.class)
    private int moderationCount;

    @JsonView(View.USER_FULL_INFO.class)
    private boolean settings;
}
