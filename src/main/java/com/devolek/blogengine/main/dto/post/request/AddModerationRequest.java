package com.devolek.blogengine.main.dto.post.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddModerationRequest {

    @JsonProperty("post_id")
    private int postId;
    private String decision;

}
