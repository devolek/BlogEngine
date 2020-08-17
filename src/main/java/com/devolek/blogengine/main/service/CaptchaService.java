package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.response.captcha.CaptchaResponse;

import java.io.IOException;

public interface CaptchaService {
    CaptchaResponse getCaptcha() throws IOException;

    boolean validateCaptcha(String code, String secret);
}
