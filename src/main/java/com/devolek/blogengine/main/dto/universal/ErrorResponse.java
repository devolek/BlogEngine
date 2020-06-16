package com.devolek.blogengine.main.dto.universal;

import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponse implements Response {
    private boolean result;
    private Map<String, String> errors;

    public ErrorResponse(Map<String, String> errors) {
        result = false;
        this.errors = errors;
    }
}
