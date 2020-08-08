package com.devolek.blogengine.main.dto.universal;

public class UniversalResponseFactory {
    public static Response getTrueResponse() {
        return new OkResponse();
    }

    public static Response getFalseResponse() {
        return new OkResponse(false);
    }

    public static Response getSingleResponse(Response response){
        return new SingleResponse(response);
    }
}
