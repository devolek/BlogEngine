package com.devolek.blogengine.main.dto.response.comments;

import com.devolek.blogengine.main.dto.response.universal.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddCommentResponse implements Response {
    int id;
}
