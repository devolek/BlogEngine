package com.devolek.blogengine.main.dto.comments.response;

import com.devolek.blogengine.main.dto.universal.Response;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddCommentResponse implements Response {
    int id;
}
