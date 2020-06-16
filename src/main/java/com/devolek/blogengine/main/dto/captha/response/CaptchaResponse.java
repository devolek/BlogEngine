package com.devolek.blogengine.main.dto.captha.response;

import com.devolek.blogengine.main.dto.universal.Response;
import lombok.Data;

@Data
public class CaptchaResponse implements Response {

    private String secret;
    private String image;

    public CaptchaResponse(String secret, String image) {
        this.secret = secret;
        this.image = image;
    }
}
