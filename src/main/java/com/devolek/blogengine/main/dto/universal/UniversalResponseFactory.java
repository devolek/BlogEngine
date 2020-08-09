package com.devolek.blogengine.main.dto.universal;

import java.util.HashMap;
import java.util.Map;

public class UniversalResponseFactory {
    public static Response getTrueResponse() {
        return new OkResponse();
    }

    public static Response getFalseResponse() {
        return new OkResponse(false);
    }

    public static Response getFileMaxSizeResponse() {
        Map<String, String> errors = new HashMap<>();
        errors.put("image", "Фото слишком большое, нужно не более 5 Мб");
        return new ErrorResponse(errors);
    }

    public static Response getSingleResponse(Response response){
        return new SingleResponse(response);
    }
}
