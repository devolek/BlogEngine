package com.devolek.blogengine.main.dto.auth.response;

import com.devolek.blogengine.main.dto.universal.Response;
import lombok.Data;

@Data
public class LoginResponse {
    private boolean result;
    private Response user;

    public LoginResponse(boolean result, Response user) {
        this.result = result;
        this.user = user;
    }
}
