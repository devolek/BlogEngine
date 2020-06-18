package com.devolek.blogengine.main.dto.post.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeRequest {
    @JsonProperty(value = "post_id")
    private int postId;
}
