package com.devolek.blogengine.main.dto.request.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeRequest {
    @JsonProperty(value = "post_id")
    private int postId;
}
