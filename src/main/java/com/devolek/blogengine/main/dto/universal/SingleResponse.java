package com.devolek.blogengine.main.dto.universal;

import lombok.Data;

@Data
public class SingleResponse implements Response{
    private boolean result;
    private Response user;

    public SingleResponse(Response user) {
        this.user = user;
        result = true;
    }
}
