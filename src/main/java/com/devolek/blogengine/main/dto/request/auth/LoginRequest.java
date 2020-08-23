package com.devolek.blogengine.main.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    @JsonProperty("e_mail")
    private String email;

    @NotBlank
    private String password;
}
