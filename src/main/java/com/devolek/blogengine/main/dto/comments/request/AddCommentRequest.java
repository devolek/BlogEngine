package com.devolek.blogengine.main.dto.comments.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddCommentRequest {
    @JsonProperty(value = "parent_id")
    private Integer parentId;

    @JsonProperty(value = "post_id")
    private int postId;

    private String text;
}
