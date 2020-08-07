package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.universal.Response;

import java.io.IOException;

public interface CaptchaService {
    Response getCaptcha() throws IOException;

    boolean validateCaptcha(String code, String secret);
}
