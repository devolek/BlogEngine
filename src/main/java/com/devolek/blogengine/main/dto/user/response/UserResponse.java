package com.devolek.blogengine.main.dto.user.response;

import com.devolek.blogengine.main.dto.universal.Response;
import lombok.Data;

@Data
public class UserResponse implements Response {
    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public UserResponse(int id, String name, String photo, String email, boolean moderation, int moderationCount, boolean settings) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.email = email;
        this.moderation = moderation;
        this.moderationCount = moderationCount;
        this.settings = settings;
    }

}
