package com.devolek.blogengine.main.dto.auth.response;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String email;
    private List<String> roles;

    public AuthResponse(String accessToken, int id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
