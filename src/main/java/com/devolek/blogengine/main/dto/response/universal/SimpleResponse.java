package com.devolek.blogengine.main.dto.response.universal;

import lombok.Data;

@Data
public class SimpleResponse implements Response{
    private boolean result;

    public SimpleResponse(boolean result) {
        this.result = result;
    }
}
