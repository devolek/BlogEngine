package com.devolek.blogengine.main.dto.response.captcha;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CaptchaResponse {
    private String secret;
    private String image;
}
