package com.devolek.blogengine.main.dto.response.universal;

import lombok.Data;

@Data
public class OkResponse implements Response{
    private boolean result;

    public OkResponse() {
        result = true;
    }

    public OkResponse(boolean result) {
        this.result = result;
    }
}
