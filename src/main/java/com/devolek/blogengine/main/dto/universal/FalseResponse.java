package com.devolek.blogengine.main.dto.universal;

import lombok.Data;

@Data
public class FalseResponse implements Response{
    private boolean result;

    public FalseResponse() {
        result = false;
    }
}
