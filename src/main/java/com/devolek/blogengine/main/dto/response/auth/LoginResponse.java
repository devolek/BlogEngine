package com.devolek.blogengine.main.dto.response.auth;

import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.user.UserDto;
import lombok.Data;

@Data
public class LoginResponse implements Response {
    private boolean result;
    private UserDto user;

    public LoginResponse(boolean result, UserDto user) {
        this.result = result;
        this.user = user;
    }
}
