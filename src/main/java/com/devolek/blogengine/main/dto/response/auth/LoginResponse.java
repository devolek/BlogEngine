package com.devolek.blogengine.main.dto.response.auth;

import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse implements Response {
    private boolean result;
    private UserDto user;
}
